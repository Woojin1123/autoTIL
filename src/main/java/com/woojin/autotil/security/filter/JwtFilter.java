package com.woojin.autotil.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woojin.autotil.auth.dto.AuthUser;
import com.woojin.autotil.auth.enums.Role;
import com.woojin.autotil.common.enums.ErrorCode;
import com.woojin.autotil.common.exception.ApiException;
import com.woojin.autotil.common.response.ApiResponse;
import com.woojin.autotil.common.util.CookieUtil;
import com.woojin.autotil.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = CookieUtil.extractTokenFromCookie(request,"access_token");

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

            } catch (ApiException e) {
                log.error("JWT 검증 실패", e);
                response.setStatus(e.getErrorCode().getHttpStatus());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
                response.setCharacterEncoding("UTF-8");

                ApiResponse errorResponse = ApiResponse.failure(e);
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return;
            } catch (ExpiredJwtException e){
                log.error("만료된 토큰입니다.");
                ApiException ex = new ApiException(ErrorCode.TOKEN_EXPIRED);
                response.setStatus(ex.getErrorCode().getHttpStatus());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
                response.setCharacterEncoding("UTF-8");

                ApiResponse errorResponse = ApiResponse.failure(ex);
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            }
        }
        filterChain.doFilter(request, response);
    }

}
