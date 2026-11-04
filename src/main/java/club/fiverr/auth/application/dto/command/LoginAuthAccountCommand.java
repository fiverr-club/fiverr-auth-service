package club.fiverr.auth.application.dto.command;

public record LoginAuthAccountCommand(
        String emailOrUsername,
        String rawPassword
) {
}
