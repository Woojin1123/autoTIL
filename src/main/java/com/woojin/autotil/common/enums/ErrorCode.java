package com.woojin.autotil.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("USER_NOT_FOUND", "사용자를 찾을 수 없습니다.", 404),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "내부 서버 오류가 발생했습니다.", 500),
    INVALID_TOKEN("INVALID_TOKEN", "유효하지 않은 토큰입니다.", 401),
    TOKEN_NOT_FOUND("TOKEN_NOT_FOUND", "인증에 필요한 토큰을 찾을 수 없습니다.", 401),
    ACCESS_DENIED("ACCESS_DENIED", "권한이 없습니다.", 403);

    private final String code;
    private final String message;
    private final int httpStatus;

    ErrorCode(String code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
    public HttpStatus getHttpStatusEnum(){
        return HttpStatus.valueOf(this.httpStatus);
    }
}
