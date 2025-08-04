package com.woojin.autotil.github.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitRepositoryDto {
    private Long id;

    private String name;

    @JsonProperty("full_name")
    private String fullName;

    private String description;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("pushed_at")
    private OffsetDateTime pushedAt;

    @JsonProperty("private")
    private boolean _private;  // "private"는 Java 예약어라 대체

    @JsonProperty("default_branch")
    private String defaultBranch;

    private Owner owner;

    private Permissions permissions;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Owner {
        private String login;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Permissions {
        private boolean admin;
        private boolean pull;
        private boolean push;
    }
}
