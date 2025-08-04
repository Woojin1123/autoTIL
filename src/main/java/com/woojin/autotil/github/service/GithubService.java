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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GithubService {
    private final RestClient githubRestClient;
    private final EncryptService encryptService;
    private final UserRepository userRepository;
    private final GithubRepository githubRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public List<GithubRepoResponse> getRepositories(String accessToken) {
        Claims claims = jwtUtil.extractClaims(accessToken);
        Long githubId = Long.valueOf(claims.getSubject());

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

        List<GitRepository> repoEntities = Arrays.stream(response)
                .map(gitRepositoryDto -> GitRepository.builder()
                        .repoName(gitRepositoryDto.getName())
                        .repoOwner(gitRepositoryDto.getOwner().getLogin())
                        .repoUrl(gitRepositoryDto.getHtmlUrl())
                        .user(authUser)
                        .build())
                .toList();

        List<GitRepository> savedRepo = githubRepository.saveAll(repoEntities);
        return savedRepo.stream()
                .map(repo -> GithubRepoResponse.from(repo))
                .toList();
    }
}
