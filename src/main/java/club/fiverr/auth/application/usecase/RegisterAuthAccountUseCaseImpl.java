package club.fiverr.auth.application.usecase;

import club.fiverr.auth.application.dto.command.RegisterAuthAccountCommand;
import club.fiverr.auth.application.dto.result.AuthTokenResult;
import club.fiverr.auth.application.mapper.AuthAccountResultMapper;
import club.fiverr.auth.application.port.in.RegisterAuthAccountUseCase;
import club.fiverr.auth.application.port.out.AuthEventPublisherPort;
import club.fiverr.auth.application.port.out.JwtTokenPort;
import club.fiverr.auth.application.port.out.PasswordEncoderPort;
import club.fiverr.auth.domain.exception.DuplicateAccountException;
import club.fiverr.auth.domain.model.AuthAccount;
import club.fiverr.auth.domain.repository.AuthAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterAuthAccountUseCaseImpl implements RegisterAuthAccountUseCase {

    private final AuthAccountRepository authAccountRepository;
    private final PasswordEncoderPort passwordEncoderPort;
    private final JwtTokenPort jwtTokenPort;
    private final AuthEventPublisherPort authEventPublisherPort;

    public RegisterAuthAccountUseCaseImpl(
            AuthAccountRepository authAccountRepository,
            PasswordEncoderPort passwordEncoderPort,
            JwtTokenPort jwtTokenPort,
            AuthEventPublisherPort authEventPublisherPort) {
        this.authAccountRepository = authAccountRepository;
        this.passwordEncoderPort = passwordEncoderPort;
        this.jwtTokenPort = jwtTokenPort;
        this.authEventPublisherPort = authEventPublisherPort;
    }

    @Override
    @Transactional
    public AuthTokenResult execute(RegisterAuthAccountCommand command) {
        String normalizedEmail = command.email().trim().toLowerCase();
        String normalizedUsername = command.username().trim().toLowerCase();

        if (authAccountRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateAccountException("Email '" + normalizedEmail + "' is already registered");
        }
        if (authAccountRepository.existsByUsername(normalizedUsername)) {
            throw new DuplicateAccountException("Username '" + normalizedUsername + "' is already taken");
        }

        String passwordHash = passwordEncoderPort.encode(command.rawPassword());
        AuthAccount account = AuthAccount.createNew(
                normalizedUsername,
                normalizedEmail,
                passwordHash,
                command.roles()
        );
        account.recordSuccessfulLogin();

        AuthAccount saved = authAccountRepository.save(account);
        authEventPublisherPort.publishAccountRegistered(saved);

        String accessToken = jwtTokenPort.generateAccessToken(saved);
        String refreshToken = jwtTokenPort.generateRefreshToken(saved);

        return new AuthTokenResult(
                accessToken,
                refreshToken,
                "Bearer",
                jwtTokenPort.getAccessTokenExpirationSeconds(),
                AuthAccountResultMapper.toResult(saved)
        );
    }
}
