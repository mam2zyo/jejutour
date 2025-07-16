package net.codecraft.jejutrip.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.codecraft.jejutrip.user.entity.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.access-token-expiration-minutes}")
    private long accessTokenExpirationMinutes;
    @Value("${jwt.refresh-token-expiration-days}")
    private long refreshTokenExpirationDays;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(String email, UserRole role) {
        return createToken(email, role, accessTokenExpirationMinutes * 60 * 1000);
    }


    public String createRefreshToken(String email, UserRole role) {
        return createToken(email, role, refreshTokenExpirationDays * 24 * 60 * 60 * 1000);
    }

    private String createToken(String email, UserRole role, long validityInMilliseconds) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .subject(email) // 토큰의 주체
                .claim("role", role.getKey()) // 커스텀 클레임 (역할 정보)
                .issuedAt(now) // 발급 시간
                .expiration(validity) // 만료 시간
                .signWith(key) // 서명 키
                .compact();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    // 토큰에서 Claims 추출
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 토큰에서 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String email = claims.getSubject();
        String role = claims.get("role", String.class);

        UserDetails userDetails = new
                User(email, "", Collections.singletonList(new SimpleGrantedAuthority(role)));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
