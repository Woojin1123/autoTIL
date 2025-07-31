package com.woojin.autotil.auth.controller;

import com.woojin.autotil.auth.dto.TokenResponse;
import com.woojin.autotil.auth.service.AuthService;
import com.woojin.autotil.common.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(
            @CookieValue("refresh_token")String refreshToken,
            HttpServletResponse response
    ){
        TokenResponse tokenResponse = authService.refresh(refreshToken);

        CookieUtil.createRefreshTokenCookie(response,tokenResponse.refreshToken());
        CookieUtil.createAccessTokenCookie(response,tokenResponse.accessToken());

        return ResponseEntity.ok("Refresh Access Token Success");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @CookieValue("refresh_token")String refreshToken,
            HttpServletResponse response
    ){
        authService.logout(refreshToken);
        CookieUtil.expireRefreshTokenCookie(response);
        CookieUtil.expireAccessTokenCookie(response);

        return ResponseEntity.ok("로그아웃 성공");
    }
}
