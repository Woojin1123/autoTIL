package com.woojin.autotil.til.controller;

import com.woojin.autotil.common.response.ApiResponse;
import com.woojin.autotil.til.dto.TilResponse;
import com.woojin.autotil.til.service.TilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/til")
public class TilController {

    private final TilService tilService;

    @PostMapping
    public ResponseEntity<ApiResponse<TilResponse>> createTilTemplate(){
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "TIL 템플릿 생성 완료",
                        tilService.createTilTemplate()
                ));
    }
}
