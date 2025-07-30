package com.woojin.autotil.security.oauth;

import com.woojin.autotil.common.util.CookieUtil;
import com.woojin.autotil.common.util.JwtUtil;
import com.woojin.autotil.user.entity.User;
import com.woojin.autotil.user.service.OAuth2LoginService;
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
    private final JwtUtil jwtUtil;
    private final OAuth2LoginService oAuth2LoginService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        User loginUser = oAuth2LoginService.oAuth2Login(customOAuth2User);

        Long userId = loginUser.getGithubId();
        String username = loginUser.getLoginId();
        String userRole = loginUser.getRole().name();

        String accessToken = jwtUtil.createToken(userId, username, userRole, false);
        String refreshToken = jwtUtil.createToken(userId, username, userRole, true);

        CookieUtil.createRefreshTokenCookie(response, refreshToken);
        CookieUtil.createAccessTokenCookie(response, accessToken); // Token을 Cookie에 넣는다.

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("OAUTH2 GIT HUB LOGIN 성공");
    }
}
