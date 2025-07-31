package com.woojin.autotil.auth.service;

import com.woojin.autotil.auth.dto.TokenResponse;
import com.woojin.autotil.auth.entity.User;
import com.woojin.autotil.auth.repository.UserRepository;
import com.woojin.autotil.common.enums.ErrorCode;
import com.woojin.autotil.common.exception.ApiException;
import com.woojin.autotil.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final RedisTokenService redisTokenService;
    private final UserRepository userRepository;

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

        redisTokenService.saveRefreshToken(newRefreshToken,user.getGithubId());

        return new TokenResponse(newAccessToken,refreshToken);
    }

    public void logout(String refreshToken) {
        if(refreshToken == null){
            throw new ApiException(ErrorCode.TOKEN_NOT_FOUND);
        }
        Claims claims = jwtUtil.extractClaims(refreshToken);
        Long githubId = Long.valueOf(claims.getSubject());

        redisTokenService.deleteRefreshToken(githubId);
    }
}
