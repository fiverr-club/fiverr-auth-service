package club.fiverr.auth.application.port.in;

import club.fiverr.auth.application.dto.command.RefreshTokenCommand;
import club.fiverr.auth.application.dto.result.AuthTokenResult;

public interface RefreshTokenUseCase {

    AuthTokenResult execute(RefreshTokenCommand command);
}
