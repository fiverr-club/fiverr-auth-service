package club.fiverr.auth.application.port.in;

import club.fiverr.auth.application.dto.command.RegisterAuthAccountCommand;
import club.fiverr.auth.application.dto.result.AuthTokenResult;

public interface RegisterAuthAccountUseCase {

    AuthTokenResult execute(RegisterAuthAccountCommand command);
}
