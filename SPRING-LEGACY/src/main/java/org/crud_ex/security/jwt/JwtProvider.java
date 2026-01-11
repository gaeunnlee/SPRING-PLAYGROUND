package org.crud_ex.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties props;
    private SecretKey key;

    @PostConstruct
    void init() {
        key = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(String userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .issuer(props.getIssuer())
                .subject(userId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(props.getAccessTtlSeconds())))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }

    // 서명/만료 검증
    public boolean validate(String token) {
        try {
            parse(token); // verifyWith(key) + exp 포함 검증
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // subject(=userId/email)만 꺼내기
    public String getSubject(String token) {
        return parse(token).getPayload().getSubject();
    }

    public Date getExpiration(String token) {
        return parse(token).getPayload().getExpiration();
    }

    public boolean isExpired(String token) {
        Date exp = getExpiration(token);
        return exp.before(new Date());
    }
}
