package com.woojin.autotil.oauth2.handler;

import com.woojin.autotil.auth.dto.CustomOAuth2User;
import com.woojin.autotil.auth.enums.Role;
import com.woojin.autotil.common.response.ApiResponse;
import com.woojin.autotil.common.util.CookieUtil;
import com.woojin.autotil.common.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler { // oauth2.0 로그인이 성공할 경우 응답
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        Long userId = customOAuth2User.getId();
        String username = String.valueOf(Long.valueOf(customOAuth2User.getName()));

        String accessToken = jwtUtil.createToken(username, Role.ROLE_USER.name(), false);
        String refreshToken = jwtUtil.createToken(username, Role.ROLE_USER.name(), true);

        ApiResponse<String> apiResponse = ApiResponse.success(
                200,
                HttpStatus.OK,
                "Access Token 발급 성공",
                accessToken);

        CookieUtil.createRefreshTokenCookie(response,refreshToken);
    }
}
