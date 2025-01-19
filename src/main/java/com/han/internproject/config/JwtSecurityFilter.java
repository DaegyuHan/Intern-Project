package com.han.internproject.config;

import com.han.internproject.domain.common.dto.AuthUser;
import com.han.internproject.domain.user.enums.UserRole;
import com.han.internproject.domain.user.service.RedisUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisUserService redisUserService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpRequest,
            @NonNull HttpServletResponse httpResponse,
            @NonNull FilterChain chain
    ) throws ServletException, IOException {
        String authorizationHeader = httpRequest.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = jwtUtil.substringToken(authorizationHeader);
            try {
                Claims claims = jwtUtil.extractClaims(jwt);
                long userId = Long.parseLong(claims.getSubject());
                String username = claims.get("username", String.class);
                UserRole userRole = UserRole.of(claims.get("userRole", String.class));

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    AuthUser authUser = new AuthUser(userId, username, userRole);

                    JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (SecurityException | MalformedJwtException e) {
                log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않는 JWT 서명입니다.");
            } catch (ExpiredJwtException e) {
                log.error("Expired JWT token, 만료된 JWT token 입니다.", e);

                Claims claims = e.getClaims();
                Long userId = Long.parseLong(claims.getSubject());
                String username = claims.get("username", String.class);
                UserRole userRole = UserRole.of(claims.get("userRole", String.class));
                log.info("만료된 Access Token 의 userId : {}", userId);

                // Redis에서 Refresh Token 조회
                String refreshToken = redisUserService.getRefreshToken(userId.toString());
                refreshToken = jwtUtil.substringToken(refreshToken);
                log.info(refreshToken);

                // Refresh Token 유효성 검사
                if (refreshToken != null) {
                    // 새로운 Access Token 발급
                    String newAccessToken = jwtUtil.createAccessToken(userId, username, userRole);

                    log.info("Access Token 재발급 완료 :::: user ID: {}", userId);
                    log.info("new Access Token : {}", newAccessToken);


                    String tokenWithoutBearer = newAccessToken.startsWith("Bearer ") ? newAccessToken.substring(7) : newAccessToken;

                    // AuthUser 객체 생성
                    AuthUser authUser = new AuthUser(userId, username, userRole);

                    // SecurityContext에 AuthUser 설정
                    Authentication authentication = new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    chain.doFilter(httpRequest, httpResponse);
                    return;
                }
            } catch (UnsupportedJwtException e) {
                log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
            } catch (Exception e) {
                log.error("Internal server error", e);
                httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        chain.doFilter(httpRequest, httpResponse);
    }
}
