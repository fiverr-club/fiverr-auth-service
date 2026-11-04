package club.fiverr.auth.application.mapper;

import club.fiverr.auth.application.dto.result.AuthAccountResult;
import club.fiverr.auth.domain.model.AuthAccount;

public final class AuthAccountResultMapper {

    private AuthAccountResultMapper() {
    }

    public static AuthAccountResult toResult(AuthAccount account) {
        return new AuthAccountResult(
                account.getId(),
                account.getUsername(),
                account.getEmail(),
                account.getRoles(),
                account.getStatus(),
                account.isEmailVerified(),
                account.getLastLoginAt(),
                account.getCreatedAt(),
                account.getUpdatedAt()
        );
    }
}
