package com.woojin.autotil.security.filter;

import com.woojin.autotil.common.util.JwtUtil;
import com.woojin.autotil.auth.dto.AuthUser;
import com.woojin.autotil.auth.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = extractTokenFromCookie(request);

        if (jwt != null) {
            try {
                Claims claims = jwtUtil.extractClaims(jwt);
                Long githubId = Long.valueOf(claims.getSubject());
                String loginId = String.valueOf(claims.get("loginId"));
                Role role = Role.of(String.valueOf(claims.get("role")));

                AuthUser authUser = AuthUser.builder()
                        .githubId(githubId)
                        .loginId(loginId)
                        .role(role)
                        .build();

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                authUser, null,
                                Collections.singletonList(new SimpleGrantedAuthority(role.name()))
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (SecurityException | MalformedJwtException e) {
                log.error("Invalid JWT signature", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 JWT 서명입니다.");
                return;
            } catch (ExpiredJwtException e) {
                log.error("Expired JWT token", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
                return;
            } catch (UnsupportedJwtException e) {
                log.error("Unsupported JWT token", e);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
                return;
            } catch (Exception e) {
                log.error("JWT 처리 실패", e);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT 토큰 처리 중 오류가 발생했습니다.");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("access-token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
