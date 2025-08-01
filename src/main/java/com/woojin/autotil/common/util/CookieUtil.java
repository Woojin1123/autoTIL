package com.woojin.autotil.common.util;

import com.woojin.autotil.common.constant.TokenTime;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

public class CookieUtil {
    public static final String REFRESH_TOKEN_NAME = "refresh_token";
    public static final String ACCESS_TOKEN_NAME = "access_token";

    public static void createRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_NAME, refreshToken)
                .httpOnly(true)
//                .secure(false) // 테스트 환경시 https아님
                .path("/")
                .maxAge(TokenTime.REFRESH_TOKEN_EXP)
                .sameSite("Lax")
                .build();
        response.addHeader("SET-COOKIE", cookie.toString());
    }

    public static void createAccessTokenCookie(HttpServletResponse response, String accessToken) {
        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_NAME, accessToken)
                .httpOnly(true)
                // .secure(false)
                .path("/")
                .maxAge(TokenTime.ACCESS_TOKEN_EXP)
                .sameSite("LAX")
                .build();
        response.addHeader("SET-COOKIE", cookie.toString());
    }

    public static void expireRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie expiredCookie = ResponseCookie.from(REFRESH_TOKEN_NAME, "")
                .httpOnly(true)
                //.secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("LAX")
                .build();
        response.addHeader("Set-Cookie", expiredCookie.toString());
    }


    public static void expireAccessTokenCookie(HttpServletResponse response) {
        ResponseCookie expiredCookie = ResponseCookie.from(ACCESS_TOKEN_NAME, "")
                .httpOnly(true)
                //.secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("LAX")
                .build();
        response.addHeader("Set-Cookie", expiredCookie.toString());
    }

    public static String extractTokenFromCookie(HttpServletRequest request, String tokenName) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (tokenName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
