package com.woojin.autotil.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {
    private int code;
    private HttpStatus httpStatus;
    private String message;
    private T data;

    public ApiResponse(int code,HttpStatus status, String message, T data ){
        this.code = code;
        this.httpStatus = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(int code, HttpStatus status, String message, T data) {
        return new ApiResponse<>(code, status, message, data);
    }
}
