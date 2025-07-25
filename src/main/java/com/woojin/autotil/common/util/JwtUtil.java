package com.woojin.autotil.common.util;

import com.woojin.autotil.common.constant.TokenTime;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${JWT_SECRET_KEY}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(String username, String role, Boolean isRefreshToken) {
        long expiration = isRefreshToken ? TokenTime.REFRESH_TOKEN_EXP : TokenTime.ACCESS_TOKEN_EXP;
        long currentTimeMills = System.currentTimeMillis();

        String token = Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .setIssuedAt(new Date(currentTimeMills))
                .setExpiration(new Date(currentTimeMills + expiration))
                .signWith(key, signatureAlgorithm)
                .compact();
        return isRefreshToken ? "Bearer " + token : token;
    }
}
