package com.woojin.autotil.common.response;

import com.woojin.autotil.common.enums.ErrorCode;
import com.woojin.autotil.common.exception.ApiException;
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

    public ApiResponse(int httpStatus, HttpStatus httpStatusEnum, String message) {
        this.code = httpStatus;
        this.httpStatus = httpStatusEnum;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(int code, HttpStatus status, String message, T data) {
        return new ApiResponse<>(code, status, message, data);
    }
    public static <T> ApiResponse<T> failure(ApiException e){
        ErrorCode errorCode = e.getErrorCode();
        return new ApiResponse<>(errorCode.getHttpStatus(),
                errorCode.getHttpStatusEnum(),
                errorCode.getMessage());
    }
}
