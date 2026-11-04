package club.fiverr.auth.application.usecase;

import club.fiverr.auth.AuthAccountTestFixtures;
import club.fiverr.auth.application.dto.command.ChangePasswordCommand;
import club.fiverr.auth.application.dto.result.AuthAccountResult;
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
class ChangePasswordUseCaseImplTest {

    @Mock
    private AuthAccountRepository authAccountRepository;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private ChangePasswordUseCaseImpl changePasswordUseCase;

    @Test
    void shouldChangePasswordWhenCurrentPasswordMatches() {
        ChangePasswordCommand command = AuthAccountTestFixtures.createValidChangePasswordCommand();
        AuthAccount account = AuthAccountTestFixtures.createValidAuthAccount();

        when(authAccountRepository.findById(command.accountId())).thenReturn(Optional.of(account));
        when(passwordEncoderPort.matches(command.currentPassword(), account.getPasswordHash())).thenReturn(true);
        when(passwordEncoderPort.encode(command.newPassword())).thenReturn("new-encoded-hash");
        when(authAccountRepository.save(any(AuthAccount.class))).thenAnswer(inv -> inv.getArgument(0));

        AuthAccountResult result = changePasswordUseCase.execute(command);

        assertNotNull(result);
        assertEquals(command.accountId(), result.id());
        verify(authAccountRepository).save(any(AuthAccount.class));
    }

    @Test
    void shouldThrowInvalidCredentialsWhenCurrentPasswordIsWrong() {
        ChangePasswordCommand command = AuthAccountTestFixtures.createValidChangePasswordCommand();
        AuthAccount account = AuthAccountTestFixtures.createValidAuthAccount();

        when(authAccountRepository.findById(command.accountId())).thenReturn(Optional.of(account));
        when(passwordEncoderPort.matches(command.currentPassword(), account.getPasswordHash())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> changePasswordUseCase.execute(command));
        verify(authAccountRepository, never()).save(any());
    }
}
