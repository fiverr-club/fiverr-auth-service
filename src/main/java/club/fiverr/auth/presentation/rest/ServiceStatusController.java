package club.fiverr.auth.presentation.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class ServiceStatusController {

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> rootStatus() {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("brand", "FIVERREX");
        response.put("service", "fiverr-auth-service");
        response.put("status", "UP");
        response.put("swaggerUi", "http://localhost:8081/swagger-ui/index.html");
        response.put("openApiDocs", "http://localhost:8081/v3/api-docs");
        return ResponseEntity.ok(response);
    }
}
