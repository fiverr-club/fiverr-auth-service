package club.fiverr.auth;

import club.fiverr.auth.application.dto.command.ChangePasswordCommand;
import club.fiverr.auth.application.dto.command.LoginAuthAccountCommand;
import club.fiverr.auth.application.dto.command.RegisterAuthAccountCommand;
import club.fiverr.auth.domain.model.AccountRole;
import club.fiverr.auth.domain.model.AccountStatus;
import club.fiverr.auth.domain.model.AuthAccount;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

public final class AuthAccountTestFixtures {

    public static final UUID DEFAULT_ACCOUNT_ID = UUID.fromString("11111111-2222-3333-4444-555555555555");

    private AuthAccountTestFixtures() {
    }

    public static AuthAccount createValidAuthAccount() {
        Instant now = Instant.now();
        return AuthAccount.restore(
                DEFAULT_ACCOUNT_ID,
                "alex_dev",
                "alex@fiverr.club",
                "$2a$12$hashedPasswordValue1234567890",
                EnumSet.of(AccountRole.BUYER, AccountRole.SELLER),
                AccountStatus.ACTIVE,
                true,
                now,
                now.minusSeconds(86400),
                now
        );
    }

    public static RegisterAuthAccountCommand createValidRegisterCommand() {
        return new RegisterAuthAccountCommand(
                "alex_dev",
                "alex@fiverr.club",
                "StrongPass#2026",
                Set.of(AccountRole.BUYER, AccountRole.SELLER)
        );
    }

    public static LoginAuthAccountCommand createValidLoginCommand() {
        return new LoginAuthAccountCommand(
                "alex@fiverr.club",
                "StrongPass#2026"
        );
    }

    public static ChangePasswordCommand createValidChangePasswordCommand() {
        return new ChangePasswordCommand(
                DEFAULT_ACCOUNT_ID,
                "StrongPass#2026",
                "NewStrongPass#2027"
        );
    }
}
