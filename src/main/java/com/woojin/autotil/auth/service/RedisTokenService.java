package com.woojin.autotil.auth.service;

import com.woojin.autotil.common.constant.TokenTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTokenService {
    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(String token,Long githubId){
        String key = "RT:" + githubId;
        redisTemplate.opsForValue().set(
                key,
                token,
                TokenTime.REFRESH_TOKEN_EXP,
                TimeUnit.MILLISECONDS
        );
    }

    public void deleteRefreshToken(Long githubId) {
        String key = "RT:" + githubId;
        redisTemplate.delete(key);
    }
}
