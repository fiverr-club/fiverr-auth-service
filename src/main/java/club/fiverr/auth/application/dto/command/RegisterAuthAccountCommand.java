package club.fiverr.auth.application.dto.command;

import club.fiverr.auth.domain.model.AccountRole;

import java.util.Set;

public record RegisterAuthAccountCommand(
        String username,
        String email,
        String rawPassword,
        Set<AccountRole> roles
) {
}
