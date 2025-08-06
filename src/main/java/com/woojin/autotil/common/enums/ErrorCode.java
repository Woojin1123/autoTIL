package com.woojin.autotil.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    NOT_FOUND("NOT_FOUND", "%s NOT FOUND.", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "내부 서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_TOKEN("INVALID_TOKEN", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("TOKEN_EXPIRED", "인증이 만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_NOT_FOUND("TOKEN_NOT_FOUND", "인증에 필요한 토큰을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("UNAUTHORIZED", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("ACCESS_DENIED", "권한이 없습니다.", HttpStatus.FORBIDDEN),
    API_REQUEST_FAILED("API_REQUEST_FAILED", "API 요청이 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "RESOURCE IS NOT FOUND", HttpStatus.NOT_FOUND),
    BAD_REQUEST("BAD_REQUEST", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
