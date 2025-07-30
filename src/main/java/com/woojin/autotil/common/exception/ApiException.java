package com.woojin.autotil.common.exception;

import com.woojin.autotil.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private ErrorCode errorCode;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
