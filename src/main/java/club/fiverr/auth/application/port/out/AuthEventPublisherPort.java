package club.fiverr.auth.application.port.out;

import club.fiverr.auth.domain.model.AuthAccount;

public interface AuthEventPublisherPort {

    void publishAccountRegistered(AuthAccount account);
}
