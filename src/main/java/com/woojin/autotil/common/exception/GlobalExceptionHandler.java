package com.woojin.autotil.common.exception;

import com.woojin.autotil.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException e) {
        ApiResponse response = ApiResponse.failure(e);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

}