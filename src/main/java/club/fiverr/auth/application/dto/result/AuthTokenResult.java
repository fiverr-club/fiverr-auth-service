package club.fiverr.auth.application.dto.result;

public record AuthTokenResult(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresInSeconds,
        AuthAccountResult account
) {
}
