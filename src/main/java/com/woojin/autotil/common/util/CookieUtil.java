package com.woojin.autotil.common.util;

import com.woojin.autotil.common.constant.TokenTime;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

public class CookieUtil {

    public static void createRefreshTokenCookie(HttpServletResponse response, String refreshToken){
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // 테스트 환경시 https아님
                .path("/")
                .maxAge(TokenTime.REFRESH_TOKEN_EXP)
                .sameSite("None")
                .build();
        response.addHeader("SET_COOKIE",cookie.toString());
    }
}
