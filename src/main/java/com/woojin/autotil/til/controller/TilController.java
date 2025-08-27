package com.woojin.autotil.til.controller;

import com.woojin.autotil.common.response.ApiResponse;
import com.woojin.autotil.til.dto.TilRequest;
import com.woojin.autotil.til.service.TilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/til")
public class TilController {

    private final TilService tilService;

    @GetMapping
    public ResponseEntity<ApiResponse> createTilTemplate(
            @RequestBody TilRequest tilRequest
    ){
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "TIL 템플릿 생성 완료",
                        tilService.createTilTemplate(tilRequest)
                ));
    }
}
