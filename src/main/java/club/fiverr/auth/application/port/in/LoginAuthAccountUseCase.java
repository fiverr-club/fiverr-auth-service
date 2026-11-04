package club.fiverr.auth.application.port.in;

import club.fiverr.auth.application.dto.command.LoginAuthAccountCommand;
import club.fiverr.auth.application.dto.result.AuthTokenResult;

public interface LoginAuthAccountUseCase {

    AuthTokenResult execute(LoginAuthAccountCommand command);
}
