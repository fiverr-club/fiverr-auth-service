package club.fiverr.auth.application.port.in;

import club.fiverr.auth.application.dto.command.ChangePasswordCommand;
import club.fiverr.auth.application.dto.result.AuthAccountResult;

public interface ChangePasswordUseCase {

    AuthAccountResult execute(ChangePasswordCommand command);
}
