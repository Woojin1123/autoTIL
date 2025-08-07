package com.woojin.autotil.github.controller;

import com.woojin.autotil.commit.dto.CommitResponse;
import com.woojin.autotil.common.response.ApiResponse;
import com.woojin.autotil.github.service.GithubService;
import com.woojin.autotil.repo.dto.GithubRepoResponse;
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
    public ResponseEntity<ApiResponse<List<GithubRepoResponse>>> getAllRepositories() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "레포지토리 로딩 성공",
                        githubService.getAllRepositories()
                ));
    }

    @GetMapping("/repos/{repoName}/commits")
    public ResponseEntity<ApiResponse<List<CommitResponse>>> getCommitsByRepo(
            @PathVariable String repoName,
            @RequestParam(required = false) LocalDateTime since,
            @RequestParam(required = false) Long perPage,
            @RequestParam(required = false) Long page
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "커밋 리스트 조회 성공",
                        githubService.getCommitsByRepo(repoName, since, perPage, page)
                )
        );
    }

    @GetMapping("/commits/{sha}/diff")
    public ResponseEntity<ApiResponse<String>> getCommitDiff(
            @PathVariable String sha
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "커밋 Diff 내역 조회 성공",
                        githubService.getCommitDiff(sha)
                )
        );
    }
}
