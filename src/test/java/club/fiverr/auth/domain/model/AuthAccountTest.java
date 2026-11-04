package club.fiverr.auth.domain.model;

import club.fiverr.auth.domain.exception.AuthDomainException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AuthAccountTest {

    @Test
    void shouldCreateNewAccountWithDefaultBuyerRoleWhenRolesEmpty() {
        AuthAccount account = AuthAccount.createNew(
                "  Freelancer_One ",
                "  Freelancer@Fiverr.Club ",
                "hashed-secret",
                Set.of()
        );

        assertNotNull(account.getId());
        assertEquals("freelancer_one", account.getUsername());
        assertEquals("freelancer@fiverr.club", account.getEmail());
        assertEquals(Set.of(AccountRole.BUYER), account.getRoles());
        assertEquals(AccountStatus.ACTIVE, account.getStatus());
        assertFalse(account.isEmailVerified());
    }

    @Test
    void shouldChangePasswordAndGrantSellerRoleWhenActive() {
        AuthAccount account = AuthAccount.createNew("bob_builder", "bob@fiverr.club", "old-hash", Set.of(AccountRole.BUYER));

        account.changePassword("new-hash");
        account.grantRole(AccountRole.SELLER);

        assertEquals("new-hash", account.getPasswordHash());
        assertTrue(account.getRoles().contains(AccountRole.SELLER));
    }

    @Test
    void shouldRejectOperationsWhenSuspended() {
        AuthAccount account = AuthAccount.createNew("suspended_user", "suspended@fiverr.club", "hash", Set.of(AccountRole.BUYER));
        account.suspend();

        assertEquals(AccountStatus.SUSPENDED, account.getStatus());
        assertThrows(AuthDomainException.class, account::recordSuccessfulLogin);
        assertThrows(AuthDomainException.class, () -> account.changePassword("another-hash"));
    }

    @Test
    void shouldRejectInvalidUsernameOrEmail() {
        assertThrows(AuthDomainException.class, () ->
                AuthAccount.createNew("ab", "valid@fiverr.club", "hash", Set.of(AccountRole.BUYER)));
        assertThrows(AuthDomainException.class, () ->
                AuthAccount.createNew("valid_user", "invalid-email", "hash", Set.of(AccountRole.BUYER)));
    }
}
