package club.fiverr.auth.infrastructure.persistence.mapper;

import club.fiverr.auth.domain.model.AuthAccount;
import club.fiverr.auth.infrastructure.persistence.entity.AuthAccountEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthAccountPersistenceMapper {

    public AuthAccountEntity toEntity(AuthAccount domain) {
        return new AuthAccountEntity(
                domain.getId(),
                domain.getUsername(),
                domain.getEmail(),
                domain.getPasswordHash(),
                domain.getRoles(),
                domain.getStatus(),
                domain.isEmailVerified(),
                domain.getLastLoginAt(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }

    public AuthAccount toDomain(AuthAccountEntity entity) {
        return AuthAccount.restore(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getRoles(),
                entity.getStatus(),
                entity.isEmailVerified(),
                entity.getLastLoginAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
