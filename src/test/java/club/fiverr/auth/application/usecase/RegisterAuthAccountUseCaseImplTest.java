package club.fiverr.auth.application.usecase;

import club.fiverr.auth.AuthAccountTestFixtures;
import club.fiverr.auth.application.dto.command.RegisterAuthAccountCommand;
import club.fiverr.auth.application.dto.result.AuthTokenResult;
import club.fiverr.auth.application.port.out.AuthEventPublisherPort;
import club.fiverr.auth.application.port.out.JwtTokenPort;
import club.fiverr.auth.application.port.out.PasswordEncoderPort;
import club.fiverr.auth.domain.exception.DuplicateAccountException;
import club.fiverr.auth.domain.model.AuthAccount;
import club.fiverr.auth.domain.repository.AuthAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterAuthAccountUseCaseImplTest {

    @Mock
    private AuthAccountRepository authAccountRepository;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Mock
    private JwtTokenPort jwtTokenPort;

    @Mock
    private AuthEventPublisherPort authEventPublisherPort;

    @InjectMocks
    private RegisterAuthAccountUseCaseImpl registerAuthAccountUseCase;

    @Test
    void shouldSuccessfullyRegisterNewAuthAccount() {
        RegisterAuthAccountCommand command = AuthAccountTestFixtures.createValidRegisterCommand();
        when(authAccountRepository.existsByEmail("alex@fiverr.club")).thenReturn(false);
        when(authAccountRepository.existsByUsername("alex_dev")).thenReturn(false);
        when(passwordEncoderPort.encode(command.rawPassword())).thenReturn("encoded-bcrypt-hash");
        when(authAccountRepository.save(any(AuthAccount.class))).thenAnswer(inv -> inv.getArgument(0));
        when(jwtTokenPort.generateAccessToken(any(AuthAccount.class))).thenReturn("jwt-access-token");
        when(jwtTokenPort.generateRefreshToken(any(AuthAccount.class))).thenReturn("jwt-refresh-token");
        when(jwtTokenPort.getAccessTokenExpirationSeconds()).thenReturn(3600L);

        AuthTokenResult result = registerAuthAccountUseCase.execute(command);

        assertNotNull(result);
        assertEquals("jwt-access-token", result.accessToken());
        assertEquals("jwt-refresh-token", result.refreshToken());
        assertEquals("alex_dev", result.account().username());
        assertEquals("alex@fiverr.club", result.account().email());
        verify(authAccountRepository).save(any(AuthAccount.class));
        verify(authEventPublisherPort).publishAccountRegistered(any(AuthAccount.class));
    }

    @Test
    void shouldThrowDuplicateAccountExceptionWhenEmailAlreadyExists() {
        RegisterAuthAccountCommand command = AuthAccountTestFixtures.createValidRegisterCommand();
        when(authAccountRepository.existsByEmail("alex@fiverr.club")).thenReturn(true);

        assertThrows(DuplicateAccountException.class, () -> registerAuthAccountUseCase.execute(command));
        verify(authAccountRepository, never()).save(any());
    }
}
