package club.fiverr.auth.application.dto.result;

import club.fiverr.auth.domain.model.AccountRole;
import club.fiverr.auth.domain.model.AccountStatus;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record AuthAccountResult(
        UUID id,
        String username,
        String email,
        Set<AccountRole> roles,
        AccountStatus status,
        boolean emailVerified,
        Instant lastLoginAt,
        Instant createdAt,
        Instant updatedAt
) {
}
