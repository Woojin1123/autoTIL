package com.woojin.autotil.security.oauth;

import com.woojin.autotil.common.util.CookieUtil;
import com.woojin.autotil.common.util.JwtUtil;
import com.woojin.autotil.user.enums.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler { // oauth2.0 로그인이 성공할 경우 응답
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        Long userId = customOAuth2User.getId();
        String username = String.valueOf(Long.valueOf(customOAuth2User.getName()));

        String accessToken = jwtUtil.createToken(username, Role.ROLE_USER.name(), false);
        String refreshToken = jwtUtil.createToken(username, Role.ROLE_USER.name(), true);

        CookieUtil.createRefreshTokenCookie(response,refreshToken);
        CookieUtil.createAccessTokenCookie(response,accessToken); // Token을 Cookie에 넣는다.
    }
}
