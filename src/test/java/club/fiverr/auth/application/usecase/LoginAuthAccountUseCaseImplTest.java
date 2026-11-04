package club.fiverr.auth.application.usecase;

import club.fiverr.auth.AuthAccountTestFixtures;
import club.fiverr.auth.application.dto.command.LoginAuthAccountCommand;
import club.fiverr.auth.application.dto.result.AuthTokenResult;
import club.fiverr.auth.application.port.out.JwtTokenPort;
import club.fiverr.auth.application.port.out.PasswordEncoderPort;
import club.fiverr.auth.domain.exception.InvalidCredentialsException;
import club.fiverr.auth.domain.model.AuthAccount;
import club.fiverr.auth.domain.repository.AuthAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginAuthAccountUseCaseImplTest {

    @Mock
    private AuthAccountRepository authAccountRepository;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Mock
    private JwtTokenPort jwtTokenPort;

    @InjectMocks
    private LoginAuthAccountUseCaseImpl loginAuthAccountUseCase;

    @Test
    void shouldSuccessfullyLoginWithValidCredentials() {
        LoginAuthAccountCommand command = AuthAccountTestFixtures.createValidLoginCommand();
        AuthAccount account = AuthAccountTestFixtures.createValidAuthAccount();

        when(authAccountRepository.findByEmail("alex@fiverr.club")).thenReturn(Optional.of(account));
        when(passwordEncoderPort.matches(command.rawPassword(), account.getPasswordHash())).thenReturn(true);
        when(authAccountRepository.save(any(AuthAccount.class))).thenAnswer(inv -> inv.getArgument(0));
        when(jwtTokenPort.generateAccessToken(any(AuthAccount.class))).thenReturn("access-token-123");
        when(jwtTokenPort.generateRefreshToken(any(AuthAccount.class))).thenReturn("refresh-token-123");
        when(jwtTokenPort.getAccessTokenExpirationSeconds()).thenReturn(3600L);

        AuthTokenResult result = loginAuthAccountUseCase.execute(command);

        assertNotNull(result);
        assertEquals("access-token-123", result.accessToken());
        assertEquals("alex_dev", result.account().username());
        verify(authAccountRepository).save(any(AuthAccount.class));
    }

    @Test
    void shouldThrowInvalidCredentialsWhenPasswordDoesNotMatch() {
        LoginAuthAccountCommand command = AuthAccountTestFixtures.createValidLoginCommand();
        AuthAccount account = AuthAccountTestFixtures.createValidAuthAccount();

        when(authAccountRepository.findByEmail("alex@fiverr.club")).thenReturn(Optional.of(account));
        when(passwordEncoderPort.matches(command.rawPassword(), account.getPasswordHash())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> loginAuthAccountUseCase.execute(command));
        verify(authAccountRepository, never()).save(any());
    }
}
