package club.fiverr.auth.domain.repository;

import club.fiverr.auth.domain.model.AuthAccount;

import java.util.Optional;
import java.util.UUID;

public interface AuthAccountRepository {

    AuthAccount save(AuthAccount account);

    Optional<AuthAccount> findById(UUID id);

    Optional<AuthAccount> findByEmail(String email);

    Optional<AuthAccount> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
