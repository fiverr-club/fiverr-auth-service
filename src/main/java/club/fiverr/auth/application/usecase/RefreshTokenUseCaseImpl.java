package club.fiverr.auth.application.usecase;

import club.fiverr.auth.application.dto.command.RefreshTokenCommand;
import club.fiverr.auth.application.dto.result.AuthTokenResult;
import club.fiverr.auth.application.mapper.AuthAccountResultMapper;
import club.fiverr.auth.application.port.in.RefreshTokenUseCase;
import club.fiverr.auth.application.port.out.JwtTokenPort;
import club.fiverr.auth.domain.exception.InvalidCredentialsException;
import club.fiverr.auth.domain.model.AuthAccount;
import club.fiverr.auth.domain.repository.AuthAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RefreshTokenUseCaseImpl implements RefreshTokenUseCase {

    private final AuthAccountRepository authAccountRepository;
    private final JwtTokenPort jwtTokenPort;

    public RefreshTokenUseCaseImpl(
            AuthAccountRepository authAccountRepository,
            JwtTokenPort jwtTokenPort) {
        this.authAccountRepository = authAccountRepository;
        this.jwtTokenPort = jwtTokenPort;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthTokenResult execute(RefreshTokenCommand command) {
        UUID accountId = jwtTokenPort.validateRefreshTokenAndExtractAccountId(command.refreshToken())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid or expired refresh token"));

        AuthAccount account = authAccountRepository.findById(accountId)
                .orElseThrow(() -> new InvalidCredentialsException("Account associated with refresh token was not found"));

        account.ensureActive();

        String newAccessToken = jwtTokenPort.generateAccessToken(account);
        String newRefreshToken = jwtTokenPort.generateRefreshToken(account);

        return new AuthTokenResult(
                newAccessToken,
                newRefreshToken,
                "Bearer",
                jwtTokenPort.getAccessTokenExpirationSeconds(),
                AuthAccountResultMapper.toResult(account)
        );
    }
}
