package club.fiverr.auth.application.port.out;

import club.fiverr.auth.domain.model.AuthAccount;

import java.util.Optional;
import java.util.UUID;

public interface JwtTokenPort {

    String generateAccessToken(AuthAccount account);

    String generateRefreshToken(AuthAccount account);

    Optional<UUID> validateRefreshTokenAndExtractAccountId(String refreshToken);

    long getAccessTokenExpirationSeconds();
}
