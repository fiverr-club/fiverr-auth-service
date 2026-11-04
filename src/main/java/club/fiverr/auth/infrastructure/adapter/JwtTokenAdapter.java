package club.fiverr.auth.infrastructure.adapter;

import club.fiverr.auth.application.port.out.JwtTokenPort;
import club.fiverr.auth.domain.model.AccountRole;
import club.fiverr.auth.domain.model.AuthAccount;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtTokenAdapter implements JwtTokenPort {

    private final SecretKey secretKey;
    private final long accessTokenExpirationSeconds;
    private final long refreshTokenExpirationSeconds;

    public JwtTokenAdapter(
            @Value("${security.jwt.secret:fiverr-club-super-secret-signing-key-must-be-at-least-256-bits-long-2026}") String secret,
            @Value("${security.jwt.access-token-expiration-seconds:3600}") long accessTokenExpirationSeconds,
            @Value("${security.jwt.refresh-token-expiration-seconds:604800}") long refreshTokenExpirationSeconds) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationSeconds = accessTokenExpirationSeconds;
        this.refreshTokenExpirationSeconds = refreshTokenExpirationSeconds;
    }

    @Override
    public String generateAccessToken(AuthAccount account) {
        Instant now = Instant.now();
        List<String> roleNames = account.getRoles().stream()
                .map(AccountRole::name)
                .toList();

        return Jwts.builder()
                .subject(account.getId().toString())
                .claim("username", account.getUsername())
                .claim("email", account.getEmail())
                .claim("roles", roleNames)
                .claim("type", "access")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTokenExpirationSeconds)))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String generateRefreshToken(AuthAccount account) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(account.getId().toString())
                .claim("type", "refresh")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(refreshTokenExpirationSeconds)))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public Optional<UUID> validateRefreshTokenAndExtractAccountId(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return Optional.empty();
        }
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();
            String tokenType = claims.get("type", String.class);
            if (!"refresh".equals(tokenType)) {
                return Optional.empty();
            }
            return Optional.of(UUID.fromString(claims.getSubject()));
        } catch (JwtException | IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    @Override
    public long getAccessTokenExpirationSeconds() {
        return accessTokenExpirationSeconds;
    }
}
