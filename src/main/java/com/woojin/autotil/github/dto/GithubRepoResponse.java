package com.woojin.autotil.github.dto;

import com.woojin.autotil.github.entity.GitRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GithubRepoResponse {
    private String repoName;
    private String repoOwner;
    private String repoUrl;

    public static GithubRepoResponse from(GitRepository repo) {
        return new GithubRepoResponse(
                repo.getRepoName(),
                repo.getRepoOwner(),
                repo.getRepoUrl()
        );
    }
}
