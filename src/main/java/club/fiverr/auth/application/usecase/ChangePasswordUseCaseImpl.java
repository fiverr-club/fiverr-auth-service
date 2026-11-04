package club.fiverr.auth.application.usecase;

import club.fiverr.auth.application.dto.command.ChangePasswordCommand;
import club.fiverr.auth.application.dto.result.AuthAccountResult;
import club.fiverr.auth.application.mapper.AuthAccountResultMapper;
import club.fiverr.auth.application.port.in.ChangePasswordUseCase;
import club.fiverr.auth.application.port.out.PasswordEncoderPort;
import club.fiverr.auth.domain.exception.AccountNotFoundException;
import club.fiverr.auth.domain.exception.InvalidCredentialsException;
import club.fiverr.auth.domain.model.AuthAccount;
import club.fiverr.auth.domain.repository.AuthAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChangePasswordUseCaseImpl implements ChangePasswordUseCase {

    private final AuthAccountRepository authAccountRepository;
    private final PasswordEncoderPort passwordEncoderPort;

    public ChangePasswordUseCaseImpl(
            AuthAccountRepository authAccountRepository,
            PasswordEncoderPort passwordEncoderPort) {
        this.authAccountRepository = authAccountRepository;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    @Transactional
    public AuthAccountResult execute(ChangePasswordCommand command) {
        AuthAccount account = authAccountRepository.findById(command.accountId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + command.accountId()));

        if (!passwordEncoderPort.matches(command.currentPassword(), account.getPasswordHash())) {
            throw new InvalidCredentialsException("Current password does not match");
        }

        String newPasswordHash = passwordEncoderPort.encode(command.newPassword());
        account.changePassword(newPasswordHash);

        AuthAccount saved = authAccountRepository.save(account);
        return AuthAccountResultMapper.toResult(saved);
    }
}
