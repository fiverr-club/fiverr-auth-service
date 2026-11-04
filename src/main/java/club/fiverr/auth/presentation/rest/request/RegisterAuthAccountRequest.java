package club.fiverr.auth.presentation.rest.request;

import club.fiverr.auth.domain.model.AccountRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record RegisterAuthAccountRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 64, message = "Username must be between 3 and 64 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Valid email format is required")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 128, message = "Password must be at least 8 characters")
        String password,

        Set<AccountRole> roles
) {
}
