package club.fiverr.auth.application.usecase;

import club.fiverr.auth.application.dto.command.LoginAuthAccountCommand;
import club.fiverr.auth.application.dto.result.AuthTokenResult;
import club.fiverr.auth.application.mapper.AuthAccountResultMapper;
import club.fiverr.auth.application.port.in.LoginAuthAccountUseCase;
import club.fiverr.auth.application.port.out.JwtTokenPort;
import club.fiverr.auth.application.port.out.PasswordEncoderPort;
import club.fiverr.auth.domain.exception.InvalidCredentialsException;
import club.fiverr.auth.domain.model.AuthAccount;
import club.fiverr.auth.domain.repository.AuthAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LoginAuthAccountUseCaseImpl implements LoginAuthAccountUseCase {

    private final AuthAccountRepository authAccountRepository;
    private final PasswordEncoderPort passwordEncoderPort;
    private final JwtTokenPort jwtTokenPort;

    public LoginAuthAccountUseCaseImpl(
            AuthAccountRepository authAccountRepository,
            PasswordEncoderPort passwordEncoderPort,
            JwtTokenPort jwtTokenPort) {
        this.authAccountRepository = authAccountRepository;
        this.passwordEncoderPort = passwordEncoderPort;
        this.jwtTokenPort = jwtTokenPort;
    }

    @Override
    @Transactional
    public AuthTokenResult execute(LoginAuthAccountCommand command) {
        String identifier = command.emailOrUsername().trim().toLowerCase();
        Optional<AuthAccount> accountOpt = identifier.contains("@")
                ? authAccountRepository.findByEmail(identifier)
                : authAccountRepository.findByUsername(identifier);

        AuthAccount account = accountOpt.orElseThrow(
                () -> new InvalidCredentialsException("Invalid email/username or password")
        );

        if (!passwordEncoderPort.matches(command.rawPassword(), account.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email/username or password");
        }

        account.recordSuccessfulLogin();
        AuthAccount saved = authAccountRepository.save(account);

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
