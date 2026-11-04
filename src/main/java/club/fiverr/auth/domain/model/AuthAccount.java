package club.fiverr.auth.domain.model;

import club.fiverr.auth.domain.exception.AuthDomainException;

import java.time.Instant;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class AuthAccount {

    private final UUID id;
    private final String username;
    private final String email;
    private String passwordHash;
    private final Set<AccountRole> roles;
    private AccountStatus status;
    private boolean emailVerified;
    private Instant lastLoginAt;
    private final Instant createdAt;
    private Instant updatedAt;

    private AuthAccount(
            UUID id,
            String username,
            String email,
            String passwordHash,
            Set<AccountRole> roles,
            AccountStatus status,
            boolean emailVerified,
            Instant lastLoginAt,
            Instant createdAt,
            Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "Account ID must not be null");
        this.username = validateUsername(username);
        this.email = validateEmail(email);
        this.passwordHash = validatePasswordHash(passwordHash);
        this.roles = (roles == null || roles.isEmpty())
                ? EnumSet.of(AccountRole.BUYER)
                : EnumSet.copyOf(roles);
        this.status = Objects.requireNonNull(status, "Account status must not be null");
        this.emailVerified = emailVerified;
        this.lastLoginAt = lastLoginAt;
        this.createdAt = Objects.requireNonNull(createdAt, "Created timestamp must not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated timestamp must not be null");
    }

    public static AuthAccount createNew(
            String username,
            String email,
            String passwordHash,
            Set<AccountRole> initialRoles) {
        Instant now = Instant.now();
        Set<AccountRole> assignedRoles = (initialRoles == null || initialRoles.isEmpty())
                ? EnumSet.of(AccountRole.BUYER)
                : EnumSet.copyOf(initialRoles);
        return new AuthAccount(
                UUID.randomUUID(),
                username,
                email,
                passwordHash,
                assignedRoles,
                AccountStatus.ACTIVE,
                false,
                null,
                now,
                now
        );
    }

    public static AuthAccount restore(
            UUID id,
            String username,
            String email,
            String passwordHash,
            Set<AccountRole> roles,
            AccountStatus status,
            boolean emailVerified,
            Instant lastLoginAt,
            Instant createdAt,
            Instant updatedAt) {
        return new AuthAccount(
                id,
                username,
                email,
                passwordHash,
                roles,
                status,
                emailVerified,
                lastLoginAt,
                createdAt,
                updatedAt
        );
    }

    public void recordSuccessfulLogin() {
        ensureActive();
        Instant now = Instant.now();
        this.lastLoginAt = now;
        this.updatedAt = now;
    }

    public void changePassword(String newPasswordHash) {
        ensureActive();
        this.passwordHash = validatePasswordHash(newPasswordHash);
        this.updatedAt = Instant.now();
    }

    public void grantRole(AccountRole role) {
        Objects.requireNonNull(role, "Role must not be null");
        ensureActive();
        this.roles.add(role);
        this.updatedAt = Instant.now();
    }

    public void suspend() {
        this.status = AccountStatus.SUSPENDED;
        this.updatedAt = Instant.now();
    }

    public void verifyEmail() {
        this.emailVerified = true;
        if (this.status == AccountStatus.PENDING_VERIFICATION) {
            this.status = AccountStatus.ACTIVE;
        }
        this.updatedAt = Instant.now();
    }

    public void ensureActive() {
        if (this.status == AccountStatus.SUSPENDED) {
            throw new AuthDomainException("Account is suspended and cannot perform this operation");
        }
    }

    private static String validateUsername(String username) {
        if (username == null || username.isBlank() || username.trim().length() < 3) {
            throw new AuthDomainException("Username must be at least 3 characters long");
        }
        return username.trim().toLowerCase();
    }

    private static String validateEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new AuthDomainException("Valid email address is required");
        }
        return email.trim().toLowerCase();
    }

    private static String validatePasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new AuthDomainException("Password hash must not be blank");
        }
        return passwordHash;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Set<AccountRole> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public AccountStatus getStatus() {
        return status;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
