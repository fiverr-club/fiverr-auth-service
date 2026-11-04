package club.fiverr.auth.infrastructure.config;

import club.fiverr.auth.application.port.out.PasswordEncoderPort;
import club.fiverr.auth.domain.model.AccountRole;
import club.fiverr.auth.domain.model.AccountStatus;
import club.fiverr.auth.domain.model.AuthAccount;
import club.fiverr.auth.domain.repository.AuthAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.EnumSet;
import java.util.UUID;

@Component
public class AuthDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AuthDataInitializer.class);

    public static final UUID ALEX_ACCOUNT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    public static final UUID ELENA_ACCOUNT_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    public static final UUID JORDAN_ACCOUNT_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");

    private final AuthAccountRepository authAccountRepository;
    private final PasswordEncoderPort passwordEncoderPort;

    public AuthDataInitializer(
            AuthAccountRepository authAccountRepository,
            PasswordEncoderPort passwordEncoderPort) {
        this.authAccountRepository = authAccountRepository;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public void run(String... args) {
        try {
            if (!authAccountRepository.findAll().isEmpty()) {
                log.info("Auth accounts already seeded, skipping initialization.");
                return;
            }

            Instant now = Instant.now();
            String defaultPasswordHash = passwordEncoderPort.encode("Fiverrex#2026");

            AuthAccount alex = AuthAccount.restore(
                    ALEX_ACCOUNT_ID,
                    "alex_architect",
                    "alex@fiverrex.club",
                    defaultPasswordHash,
                    EnumSet.of(AccountRole.BUYER, AccountRole.SELLER),
                    AccountStatus.ACTIVE,
                    true,
                    now,
                    now,
                    now
            );

            AuthAccount elena = AuthAccount.restore(
                    ELENA_ACCOUNT_ID,
                    "elena_ai",
                    "elena@fiverrex.club",
                    defaultPasswordHash,
                    EnumSet.of(AccountRole.BUYER, AccountRole.SELLER),
                    AccountStatus.ACTIVE,
                    true,
                    now,
                    now,
                    now
            );

            AuthAccount jordan = AuthAccount.restore(
                    JORDAN_ACCOUNT_ID,
                    "jordan_taylor",
                    "jordan@fiverrex.club",
                    defaultPasswordHash,
                    EnumSet.of(AccountRole.BUYER),
                    AccountStatus.ACTIVE,
                    true,
                    now,
                    now,
                    now
            );

            authAccountRepository.save(alex);
            authAccountRepository.save(elena);
            authAccountRepository.save(jordan);
            log.info("Seeded 3 default FIVERREX auth accounts.");
        } catch (Exception ex) {
            log.warn("Failed to seed default auth accounts (non-fatal): {}", ex.getMessage());
        }
    }
}
