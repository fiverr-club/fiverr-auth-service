package club.fiverr.auth.application.usecase;

import club.fiverr.auth.application.dto.result.AuthAccountResult;
import club.fiverr.auth.application.mapper.AuthAccountResultMapper;
import club.fiverr.auth.application.port.in.GetAuthAccountUseCase;
import club.fiverr.auth.domain.exception.AccountNotFoundException;
import club.fiverr.auth.domain.model.AuthAccount;
import club.fiverr.auth.domain.repository.AuthAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GetAuthAccountUseCaseImpl implements GetAuthAccountUseCase {

    private final AuthAccountRepository authAccountRepository;

    public GetAuthAccountUseCaseImpl(AuthAccountRepository authAccountRepository) {
        this.authAccountRepository = authAccountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthAccountResult getById(UUID accountId) {
        AuthAccount account = authAccountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));
        return AuthAccountResultMapper.toResult(account);
    }
}
