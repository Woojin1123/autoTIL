package com.woojin.autotil.github.service;

import com.woojin.autotil.auth.entity.User;
import com.woojin.autotil.auth.service.AuthService;
import com.woojin.autotil.common.enums.ErrorCode;
import com.woojin.autotil.common.exception.ApiException;
import com.woojin.autotil.common.util.JwtUtil;
import com.woojin.autotil.github.dto.*;
import com.woojin.autotil.github.entity.Commit;
import com.woojin.autotil.github.entity.GitRepository;
import com.woojin.autotil.github.repository.GithubCommitRepository;
import com.woojin.autotil.github.repository.GithubRepoRepository;
import com.woojin.autotil.security.oauth.EncryptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubService {
    private final RestClient githubRestClient;
    private final EncryptService encryptService;
    private final AuthService authService;
    private final GithubRepoRepository githubRepoRepository;
    private final GithubCommitRepository githubCommitRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public List<GithubRepoResponse> getRepositories() {
        User authUser = authService.getAuthUser();

        String decryptToken = encryptService.decryptToken(authUser.getGithubToken());

        //인증된 유저에 대한 Git Repo 호출
        GitRepositoryDto[] response = githubRestClient.get()
                .uri("/user/repos")
                .headers(h -> h.setBearerAuth(decryptToken))
                .retrieve()
                .body(GitRepositoryDto[].class);

        List<GitRepository> existRepo = githubRepoRepository.findAllByUser(authUser);

        Map<String, GitRepository> existByName = existRepo.stream()
                .collect(Collectors.toMap(
                        repo -> repo.getGithubRepoId().toString(),
                        repo -> repo)
                );

        List<GitRepository> repoEntities = Arrays.stream(response)
                .map(gitRepositoryDto ->
                {
                    String repoId = gitRepositoryDto.getId().toString();
                    if (existByName.containsKey(repoId)) {
                        GitRepository repo = existByName.get(repoId);
                        repo.updatePushedAt(gitRepositoryDto.getPushedAt().toLocalDateTime());
                        return repo;
                    } else {
                        return GitRepository.builder()
                                .githubRepoId(gitRepositoryDto.getId())
                                .repoName(gitRepositoryDto.getName())
                                .repoOwner(gitRepositoryDto.getOwner().getLogin())
                                .repoUrl(gitRepositoryDto.getHtmlUrl())
                                .pushed_at(
                                        gitRepositoryDto.getPushedAt().toLocalDateTime()
                                )
                                .user(authUser)
                                .build();
                    }
                })
                .toList();

        List<GitRepository> savedRepo = githubRepoRepository.saveAll(repoEntities);

        return savedRepo.stream()
                .map(GithubRepoResponse::from)
                .toList();
    }

    @Transactional
    public List<Long> updateRepoTracked(repoTrackRequest request) {
        User authUser = authService.getAuthUser();

        List<GitRepository> repos = githubRepoRepository.findAllByUserIdAndGithubRepoIdIn(authUser.getId(), request.getRepoIds());

        repos.forEach(repo -> repo.updateTracked(true));

        githubRepoRepository.saveAll(repos);

        return repos.stream()
                .filter(GitRepository::getIsTracked)
                .map(GitRepository::getId)
                .toList();
    }

    @Transactional
    public List<CommitResponse> getCommitsByRepo(Long repoId, LocalDateTime sinceDate) {
        User user = authService.getAuthUser();

        GitRepository gitRepository = githubRepoRepository.findByIdAndUserId(repoId, user.getId()).orElseThrow(() ->
                new ApiException(ErrorCode.REPOSITORY_NOT_FOUND)
        );

        String decryptToken = encryptService.decryptToken(user.getGithubToken());

        /*
        since: 날짜 기준 default 7일전 커밋까지
        author: 커밋 작성자 gitRepository 연관관계 User 기준
         */
        if (sinceDate == null) {
            sinceDate = LocalDateTime.now().minusDays(7);
        }
        OffsetDateTime since = sinceDate.atOffset(OffsetDateTime.now().getOffset());

        GitCommitDto[] response = githubRestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/repos/{owner}/{repo}/commits")
                        .queryParam("since", since)
                        .queryParam("author", gitRepository.getUser().getId())
                        .build(
                                gitRepository.getRepoOwner(),
                                gitRepository.getRepoName())
                )
                .headers(h -> h.setBearerAuth(decryptToken))
                .retrieve()
                .body(GitCommitDto[].class);

        List<Commit> existCommit = githubCommitRepository.findAllByRepositoryId(gitRepository.getId());

        Map<String, Commit> existBySha = existCommit.stream()
                .collect(Collectors.toMap(
                        Commit::getCommitSha,
                        commit -> commit)
                );

        List<Commit> commits = Arrays.stream(response).map(gitCommitDto ->
                {
                    String commitSha = gitCommitDto.getSha();
                    if (existBySha.containsKey(commitSha)) {
                        Commit commit = existBySha.get(commitSha);
                        return commit;
                    } else {
                        return Commit.builder()
                                .commitSha(gitCommitDto.getSha())
                                .committedAt(gitCommitDto.getCommit().getAuthor()
                                        .getDate().toLocalDateTime())
                                .authorLogin(gitCommitDto.getAuthor()
                                        .getLogin())
                                .htmlUrl(gitCommitDto.getHtmlUrl())
                                .message(gitCommitDto.getCommit()
                                        .getMessage())
                                .gitRepository(gitRepository)
                                .build();
                    }
                })
                .toList();

        List<Commit> savedCommits = githubCommitRepository.saveAll(commits);

        return savedCommits.stream()
                .map(CommitResponse::from)
                .toList();
    }

}
