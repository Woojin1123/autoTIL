package com.woojin.autotil.common.exception;

import com.woojin.autotil.common.enums.ErrorCode;
import com.woojin.autotil.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(ApiException e) {
        ApiResponse<?> response = ApiResponse.failure(e);
        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(PropertyValueException.class)
    public ResponseEntity<?> handlePropertyException(PropertyValueException ex) {
        log.error("Hibernate property null 에러: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(new ApiException(ErrorCode.BAD_REQUEST)));
    }

}