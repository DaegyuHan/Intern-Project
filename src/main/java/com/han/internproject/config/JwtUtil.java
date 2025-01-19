package com.han.internproject.config;

import com.han.internproject.domain.user.enums.UserRole;
import com.han.internproject.domain.user.service.RedisUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.rmi.ServerException;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_TIME = 15 * 60 * 1000L; // 15분
    private static final long REFRESH_TOKEN_TIME = 24 * 60 * 60 * 1000L; // 1일

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private final RedisUserService redisUserService;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // Access Token 생성
    public String createAccessToken(Long userId, String username, UserRole userRole) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("username", username)
                        .claim("userRole", userRole)
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // Refresh Token 생성
    public String createRefreshToken(Long userId, String username, UserRole userRole) {
        Date date = new Date();

        String refreshToken =  BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("username", username)
                        .claim("userRole", userRole)
                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm)
                        .compact();

        redisUserService.saveRefreshToken(userId.toString(), refreshToken, REFRESH_TOKEN_TIME);
        return refreshToken;
    }

    public String substringToken(String tokenValue) throws ServerException {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new ServerException("Not Found Token");
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
