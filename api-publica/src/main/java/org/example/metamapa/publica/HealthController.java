package org.example.metamapa.publica;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HealthController {
    @GetMapping("/ping")
    public String ping() {
        return "api-publica OK";
    }
}
