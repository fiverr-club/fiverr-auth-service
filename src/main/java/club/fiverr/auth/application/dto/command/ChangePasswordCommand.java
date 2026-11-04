package club.fiverr.auth.application.dto.command;

import java.util.UUID;

public record ChangePasswordCommand(
        UUID accountId,
        String currentPassword,
        String newPassword
) {
}
