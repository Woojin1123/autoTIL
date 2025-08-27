package com.woojin.autotil.repo.dto;

import com.woojin.autotil.repo.entity.GitRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GithubRepoResponse {
    private Long id;
    private Long githubRepoId;
    private String repoName;
    private String repoOwner;
    private String repoUrl;

    public static GithubRepoResponse from(GitRepository repo) {
        return new GithubRepoResponse(
                repo.getId(),
                repo.getGithubRepoId(),
                repo.getRepoName(),
                repo.getRepoOwner(),
                repo.getRepoUrl()
        );
    }
}
