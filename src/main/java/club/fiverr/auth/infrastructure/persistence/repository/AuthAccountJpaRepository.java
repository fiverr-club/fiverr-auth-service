package club.fiverr.auth.infrastructure.persistence.repository;

import club.fiverr.auth.infrastructure.persistence.entity.AuthAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthAccountJpaRepository extends JpaRepository<AuthAccountEntity, UUID> {

    Optional<AuthAccountEntity> findByEmail(String email);

    Optional<AuthAccountEntity> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
