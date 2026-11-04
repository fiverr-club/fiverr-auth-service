package club.fiverr.auth.infrastructure.adapter;

import club.fiverr.auth.application.port.out.AuthEventPublisherPort;
import club.fiverr.auth.domain.model.AuthAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

@Component
public class KafkaAuthEventPublisherAdapter implements AuthEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaAuthEventPublisherAdapter.class);
    private static final String TOPIC_ACCOUNT_REGISTERED = "fiverr.auth.account-registered";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaAuthEventPublisherAdapter(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishAccountRegistered(AuthAccount account) {
        Map<String, Object> eventPayload = Map.of(
                "eventType", "ACCOUNT_REGISTERED",
                "accountId", account.getId().toString(),
                "username", account.getUsername(),
                "email", account.getEmail(),
                "roles", account.getRoles(),
                "occurredAt", Instant.now().toString()
        );
        try {
            kafkaTemplate.send(TOPIC_ACCOUNT_REGISTERED, account.getId().toString(), eventPayload);
        } catch (Exception ex) {
            log.warn("Non-blocking Kafka publish warning for account {}: {}", account.getId(), ex.getMessage());
        }
    }
}
