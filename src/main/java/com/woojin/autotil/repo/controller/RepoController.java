package com.woojin.autotil.repo.controller;

import com.woojin.autotil.common.response.ApiResponse;
import com.woojin.autotil.repo.dto.GithubRepoResponse;
import com.woojin.autotil.repo.dto.RepoTrackRequest;
import com.woojin.autotil.repo.service.RepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RepoController {
    private final RepoService repoService;

    @PatchMapping("/repos")
    public ResponseEntity<ApiResponse<List<Long>>> updateRepoTracked(
            @RequestBody RepoTrackRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "레포지토리 업데이트 성공",
                        repoService.updateRepoTracked(request)
                ));
    }

    @GetMapping("/repos/tracking")
    public ResponseEntity<ApiResponse<List<GithubRepoResponse>>> getTrackingRepo() {
        return ResponseEntity.ok().body(ApiResponse.success(
                HttpStatus.OK,
                "추적 중인 레포 조회 성공",
                repoService.getTrackingRepo()
                )
        );
    }
}
