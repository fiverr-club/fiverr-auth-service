package club.fiverr.auth.application.port.in;

import club.fiverr.auth.application.dto.result.AuthAccountResult;

import java.util.UUID;

public interface GetAuthAccountUseCase {

    AuthAccountResult getById(UUID accountId);
}
