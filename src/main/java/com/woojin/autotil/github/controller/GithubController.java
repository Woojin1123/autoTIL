package com.woojin.autotil.github.controller;

import com.woojin.autotil.common.response.ApiResponse;
import com.woojin.autotil.github.dto.CommitResponse;
import com.woojin.autotil.github.dto.GithubRepoResponse;
import com.woojin.autotil.github.service.GithubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GithubController {

    private final GithubService githubService;

    @GetMapping("/repos")
    public ResponseEntity<ApiResponse<List<GithubRepoResponse>>> getRepositories(
            @CookieValue("access_token") String accessToken
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        HttpStatus.OK,
                        "레포지토리 로딩 성공",
                        githubService.getRepositories(accessToken)
                ));
    }

    @GetMapping("/repos/{repoId}/commit")
    public ResponseEntity<ApiResponse<List<CommitResponse>>> getCommitsByRepo(
            @PathVariable String repoId,
            @RequestParam(required = false) LocalDateTime since
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        HttpStatus.OK,
                        "커밋 리스트 조회 성공",
                        githubService.getCommitsByRepo(repoId, since)
                )
        );

    }
}
