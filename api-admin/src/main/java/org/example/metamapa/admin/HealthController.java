package org.example.metamapa.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HealthController {
    @GetMapping("/ping")
    String ping() { return "api-admin OK"; }
}
