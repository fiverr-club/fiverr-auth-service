package club.fiverr.auth.presentation.rest.response;

public record AuthTokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresInSeconds,
        AuthAccountResponse account
) {
}
