package club.fiverr.auth.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI authServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FIVERREX. — Auth & IAM Service API")
                        .version("1.0.0")
                        .description("Authentication, JWT issuance, and Identity Access Management microservice for FIVERREX.")
                        .contact(new Contact().name("FIVERREX Engineering").email("engineering@fiverrex.club")));
    }
}
