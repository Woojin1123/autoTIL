package com.woojin.autotil.repo.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class RepoTrackRequest {
    List<Long> repoIds;
}
