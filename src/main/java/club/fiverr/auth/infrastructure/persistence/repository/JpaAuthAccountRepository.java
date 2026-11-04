package club.fiverr.auth.infrastructure.persistence.repository;

import club.fiverr.auth.domain.model.AuthAccount;
import club.fiverr.auth.domain.repository.AuthAccountRepository;
import club.fiverr.auth.infrastructure.persistence.entity.AuthAccountEntity;
import club.fiverr.auth.infrastructure.persistence.mapper.AuthAccountPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaAuthAccountRepository implements AuthAccountRepository {

    private final AuthAccountJpaRepository jpaRepository;
    private final AuthAccountPersistenceMapper mapper;

    public JpaAuthAccountRepository(
            AuthAccountJpaRepository jpaRepository,
            AuthAccountPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public AuthAccount save(AuthAccount account) {
        AuthAccountEntity entity = mapper.toEntity(account);
        AuthAccountEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<AuthAccount> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<AuthAccount> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<AuthAccount> findByUsername(String username) {
        return jpaRepository.findByUsername(username).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }
}
