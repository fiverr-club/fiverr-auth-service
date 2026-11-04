package club.fiverr.auth.presentation.rest.request;

import jakarta.validation.constraints.NotBlank;

public record LoginAuthAccountRequest(
        @NotBlank(message = "Email or username is required")
        String emailOrUsername,

        @NotBlank(message = "Password is required")
        String password
) {
}
