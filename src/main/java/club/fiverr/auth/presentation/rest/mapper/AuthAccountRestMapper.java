package club.fiverr.auth.presentation.rest.mapper;

import club.fiverr.auth.application.dto.command.ChangePasswordCommand;
import club.fiverr.auth.application.dto.command.LoginAuthAccountCommand;
import club.fiverr.auth.application.dto.command.RefreshTokenCommand;
import club.fiverr.auth.application.dto.command.RegisterAuthAccountCommand;
import club.fiverr.auth.application.dto.result.AuthAccountResult;
import club.fiverr.auth.application.dto.result.AuthTokenResult;
import club.fiverr.auth.presentation.rest.request.ChangePasswordRequest;
import club.fiverr.auth.presentation.rest.request.LoginAuthAccountRequest;
import club.fiverr.auth.presentation.rest.request.RefreshTokenRequest;
import club.fiverr.auth.presentation.rest.request.RegisterAuthAccountRequest;
import club.fiverr.auth.presentation.rest.response.AuthAccountResponse;
import club.fiverr.auth.presentation.rest.response.AuthTokenResponse;

import java.util.UUID;

public final class AuthAccountRestMapper {

    private AuthAccountRestMapper() {
    }

    public static RegisterAuthAccountCommand toCommand(RegisterAuthAccountRequest request) {
        return new RegisterAuthAccountCommand(
                request.username(),
                request.email(),
                request.password(),
                request.roles()
        );
    }

    public static LoginAuthAccountCommand toCommand(LoginAuthAccountRequest request) {
        return new LoginAuthAccountCommand(
                request.emailOrUsername(),
                request.password()
        );
    }

    public static RefreshTokenCommand toCommand(RefreshTokenRequest request) {
        return new RefreshTokenCommand(request.refreshToken());
    }

    public static ChangePasswordCommand toCommand(UUID accountId, ChangePasswordRequest request) {
        return new ChangePasswordCommand(
                accountId,
                request.currentPassword(),
                request.newPassword()
        );
    }

    public static AuthAccountResponse toResponse(AuthAccountResult result) {
        return new AuthAccountResponse(
                result.id(),
                result.username(),
                result.email(),
                result.roles(),
                result.status(),
                result.emailVerified(),
                result.lastLoginAt(),
                result.createdAt(),
                result.updatedAt()
        );
    }

    public static AuthTokenResponse toResponse(AuthTokenResult result) {
        return new AuthTokenResponse(
                result.accessToken(),
                result.refreshToken(),
                result.tokenType(),
                result.expiresInSeconds(),
                toResponse(result.account())
        );
    }
}
