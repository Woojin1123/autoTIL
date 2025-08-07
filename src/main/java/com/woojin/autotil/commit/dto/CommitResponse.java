package com.woojin.autotil.commit.dto;

import com.woojin.autotil.commit.entity.Commit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommitResponse {
    private String sha;
    private String htmlUrl;
    private String authorLogin;
    private LocalDateTime committedAt;
    private String message;

    public static CommitResponse from(Commit commit) {
        return new CommitResponse(
                commit.getCommitSha(),
                commit.getHtmlUrl(),
                commit.getHtmlUrl(),
                commit.getCommittedAt(),
                commit.getMessage()
        );
    }
}
