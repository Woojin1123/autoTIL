package com.woojin.autotil.auth.service;

import com.woojin.autotil.auth.dto.TokenResponse;
import com.woojin.autotil.auth.entity.User;
import com.woojin.autotil.auth.repository.UserRepository;
import com.woojin.autotil.common.enums.ErrorCode;
import com.woojin.autotil.common.exception.ApiException;
import com.woojin.autotil.common.util.JwtUtil;
import com.woojin.autotil.security.oauth.EncryptService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${GITHUB_CLIENT_ID}")
    private String clientId;
    @Value("${GITHUB_CLIENT_SECRET}")
    private String clientSecret;
    private final JwtUtil jwtUtil;
    private final EncryptService encryptService;
    private final RedisTokenService redisTokenService;
    private final UserRepository userRepository;
    private final RestClient githubRestClient;


    public TokenResponse refresh(String refreshToken) {
        if (refreshToken == null) {
            throw new ApiException(ErrorCode.TOKEN_NOT_FOUND);
        }

        Claims claims = jwtUtil.extractClaims(refreshToken);
        Long githubId = Long.valueOf(claims.getSubject());

        String redisToken = redisTokenService.getToken(githubId);

        if (!refreshToken.equals(redisToken)) {
            throw new ApiException(ErrorCode.INVALID_TOKEN);
        }
        User user = userRepository.findByGithubId(githubId).orElseThrow(() ->
                new ApiException(ErrorCode.USER_NOT_FOUND)
        );

        String newAccessToken = jwtUtil.createToken(
                user.getGithubId(),
                user.getLoginId(),
                user.getRole().name(),
                false);

        String newRefreshToken = jwtUtil.createToken(
                user.getGithubId(),
                user.getLoginId(),
                user.getRole().name(),
                true);

        redisTokenService.saveRefreshToken(newRefreshToken, user.getGithubId());

        return new TokenResponse(newAccessToken, refreshToken);
    }

    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken == null) {
            throw new ApiException(ErrorCode.TOKEN_NOT_FOUND);
        }
        Claims claims = jwtUtil.extractClaims(refreshToken);
        Long githubId = Long.valueOf(claims.getSubject());
        User user = userRepository.findByGithubId(githubId).orElseThrow(() ->
                new ApiException(ErrorCode.USER_NOT_FOUND));

        String decryptToken = encryptService.decryptToken(user.getGithubToken());

        githubRestClient
                .method(HttpMethod.DELETE)
                .uri("/applications/{clientId}/token", clientId)
                .body(Map.of("access_token", decryptToken))
                .retrieve()
                .toBodilessEntity();

        redisTokenService.deleteRefreshToken(githubId);
        user.revokedGithubToken();
        userRepository.save(user);
    }
}
