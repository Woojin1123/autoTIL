package com.woojin.autotil.common.response;

import com.woojin.autotil.common.exception.ApiException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {
    private HttpStatus httpStatus;
    private String message;
    private T data;

    public ApiResponse(HttpStatus status, String message, T data ){
        this.httpStatus = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(HttpStatus httpStatusEnum, String message) {
        this.httpStatus = httpStatusEnum;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }
    public static <T> ApiResponse<T> failure(ApiException e){
        return new ApiResponse<>(
                e.getHttpStatus(),
                e.getMessage());
    }
}
