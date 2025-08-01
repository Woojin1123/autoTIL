package com.woojin.autotil.auth.controller;

import com.woojin.autotil.auth.dto.TokenResponse;
import com.woojin.autotil.auth.service.AuthService;
import com.woojin.autotil.common.response.ApiResponse;
import com.woojin.autotil.common.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    @GetMapping("/refresh")
    public ResponseEntity<ApiResponse> refresh(
            @CookieValue("refresh_token")String refreshToken,
            HttpServletResponse response
    ){
        TokenResponse tokenResponse = authService.refresh(refreshToken);

        CookieUtil.createRefreshTokenCookie(response,tokenResponse.refreshToken());
        CookieUtil.createAccessTokenCookie(response,tokenResponse.accessToken());

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        HttpStatus.OK,
                        "Refresh Access Token Success",
                        null
                ));
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse> logout(
            @CookieValue("refresh_token")String refreshToken,
            HttpServletResponse response
    ){
        authService.logout(refreshToken);
        CookieUtil.expireRefreshTokenCookie(response);
        CookieUtil.expireAccessTokenCookie(response);

        return ResponseEntity.ok(
                ApiResponse.success(
                        205,
                        HttpStatus.RESET_CONTENT,
                        "로그아웃 성공",
                        null));
    }
}
