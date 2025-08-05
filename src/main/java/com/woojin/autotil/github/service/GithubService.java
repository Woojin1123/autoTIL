package com.woojin.autotil.github.service;

import com.woojin.autotil.auth.entity.User;
import com.woojin.autotil.auth.repository.UserRepository;
import com.woojin.autotil.common.enums.ErrorCode;
import com.woojin.autotil.common.exception.ApiException;
import com.woojin.autotil.common.util.JwtUtil;
import com.woojin.autotil.github.dto.GitRepositoryDto;
import com.woojin.autotil.github.dto.GithubRepoResponse;
import com.woojin.autotil.github.entity.GitRepository;
import com.woojin.autotil.github.repository.GithubRepository;
import com.woojin.autotil.security.oauth.EncryptService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

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
    private final UserRepository userRepository;
    private final GithubRepository githubRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public List<GithubRepoResponse> getRepositories() {
        AuthUser principal = (AuthUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Long githubId = principal.getGithubId();

        User authUser = userRepository.findByGithubId(githubId).orElseThrow(() ->
                new ApiException(ErrorCode.USER_NOT_FOUND)
        );

        String decryptToken = encryptService.decryptToken(authUser.getGithubToken());

        //인증된 유저에 대한 Git Repo 호출
        GitRepositoryDto[] response = githubRestClient.get()
                .uri("/user/repos")
                .headers(h -> h.setBearerAuth(decryptToken))
                .retrieve()
                .body(GitRepositoryDto[].class);

        List<GitRepository> existRepo = githubRepository.findAllByUser(authUser);

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

        List<GitRepository> savedRepo = githubRepository.saveAll(repoEntities);

        return savedRepo.stream()
                .map(GithubRepoResponse::from)
                .toList();
    }

    @Transactional
    public List<Long> updateRepoTracked(repoTrackRequest request) {
        AuthUser principal = (AuthUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User authUser = userRepository.findByGithubId(principal.getGithubId()).orElseThrow(() ->
                new ApiException(ErrorCode.USER_NOT_FOUND)
        );

        List<GitRepository> repos = githubRepository.findAllByUserIdAndGithubRepoIdIn(authUser.getId(), request.getRepoIds());

        repos.forEach(repo -> repo.updateTracked(true));

        githubRepository.saveAll(repos);

        return repos.stream()
                .filter(GitRepository::getIsTracked)
                .map(GitRepository::getId)
                .toList();
    }

    public List<CommitResponse> getCommitsByRepo(String repoId, LocalDateTime since) {

        return null;
    }
}
