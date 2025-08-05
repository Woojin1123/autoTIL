package com.woojin.autotil.github.service;

import com.woojin.autotil.auth.entity.User;
import com.woojin.autotil.auth.repository.UserRepository;
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
import java.time.ZoneOffset;
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
    private final UserRepository userRepository;
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
                        GitRepository newGitRepo = GitRepository.builder()
                                .githubRepoId(gitRepositoryDto.getId())
                                .repoName(gitRepositoryDto.getName())
                                .repoOwner(gitRepositoryDto.getOwner().getLogin())
                                .repoUrl(gitRepositoryDto.getHtmlUrl())
                                .pushed_at(
                                        gitRepositoryDto.getPushedAt().toLocalDateTime()
                                )
                                .user(authUser)
                                .build();
                        authUser.addGitRepo(newGitRepo);
                        return newGitRepo;
                    }
                })
                .toList();

        List<GitRepository> savedRepo = githubRepoRepository.saveAll(repoEntities);
        userRepository.save(authUser);

        return savedRepo.stream()
                .map(GithubRepoResponse::from)
                .toList();
    }

    @Transactional
    public List<Long> updateRepoTracked(RepoTrackRequest request) {
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
    public List<CommitResponse> getCommitsByRepo(Long repoId, LocalDateTime sinceDate,Long perPage, Long page) {
        User user = authService.getAuthUser();

        GitRepository gitRepo = githubRepoRepository.findByIdAndUserId(repoId, user.getId()).orElseThrow(() ->
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
        OffsetDateTime since = sinceDate.atOffset(ZoneOffset.UTC);

        GitCommitDto[] response = githubRestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/repos/{owner}/{repo}/commits")
                        .queryParam("since", since)
                        .queryParam("author", gitRepo.getUser().getLoginId())
                        .queryParam("per_page",perPage) // Default 30
                        .queryParam("page",page) // Default 1
                        .build(
                                gitRepo.getRepoOwner(),
                                gitRepo.getRepoName())
                )
                .headers(h -> h.setBearerAuth(decryptToken))
                .retrieve()
                .body(GitCommitDto[].class);

        List<Commit> existCommits = githubCommitRepository.findAllByRepositoryId(gitRepo.getId());

        Map<String, Commit> existBySha = existCommits.stream()
                .collect(Collectors.toMap(
                        Commit::getCommitSha,
                        commit -> commit)
                );

        List<Commit> commits = Arrays.stream(response).map(gitCommitDto ->
                {
                    String commitSha = gitCommitDto.getSha();
                    if (existBySha.containsKey(commitSha)) {
                        Commit existCommit = existBySha.get(commitSha);
                        return existCommit;
                    } else {
                        Commit newCommit = Commit.builder()
                                .commitSha(gitCommitDto.getSha())
                                .committedAt(gitCommitDto.getCommit().getAuthor()
                                        .getDate().toLocalDateTime())
                                .authorLogin(gitCommitDto.getAuthor()
                                        .getLogin())
                                .htmlUrl(gitCommitDto.getHtmlUrl())
                                .message(gitCommitDto.getCommit()
                                        .getMessage())
                                .gitRepository(gitRepo)
                                .build();
                        gitRepo.addCommit(newCommit);
                        return newCommit;
                    }
                })
                .toList();

        List<Commit> savedCommits = githubCommitRepository.saveAll(commits);
        githubRepoRepository.save(gitRepo);

        return savedCommits.stream()
                .map(CommitResponse::from)
                .toList();
    }

}
