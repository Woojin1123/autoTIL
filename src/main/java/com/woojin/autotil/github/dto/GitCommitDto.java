package com.woojin.autotil.github.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitCommitDto {
    private String sha;
    @JsonProperty("html_url")
    private String htmlUrl;
    private CommitInfo commit;
    private userDto author;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class userDto {
        private String login;
        private Long id;
        private String url;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CommitInfo {
        private String url;
        private String message;
        private AuthorDto author;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AuthorDto {
        private OffsetDateTime date;
    }
}
