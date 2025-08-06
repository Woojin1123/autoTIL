package com.woojin.autotil.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("USER_NOT_FOUND", "사용자를 찾을 수 없습니다.", 404),
    REPOSITORY_NOT_FOUND("REPOSITORY_NOT_FOUND", "존재하지 않는 레포지토리 입니다.", 404),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "내부 서버 오류가 발생했습니다.", 500),
    INVALID_TOKEN("INVALID_TOKEN", "유효하지 않은 토큰입니다.", 401),
    TOKEN_EXPIRED("TOKEN_EXPIRED", "인증이 만료된 토큰입니다.", 401),
    TOKEN_NOT_FOUND("TOKEN_NOT_FOUND", "인증에 필요한 토큰을 찾을 수 없습니다.", 401),
    UNAUTHORIZED("UNAUTHORIZED", "인증이 필요합니다.", 401),
    ACCESS_DENIED("ACCESS_DENIED", "권한이 없습니다.", 403),
    API_REQUEST_FAILED("API_REQUEST_FAILED", "API 요청이 실패했습니다.", 500),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "Resource is not found", 404),
    BAD_REQUEST("BAD_REQUEST", "잘못된 요청입니다.", 400);


    private final String code;
    private final String message;
    private final int httpStatus;

    ErrorCode(String code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatusEnum() {
        return HttpStatus.valueOf(this.httpStatus);
    }
}
