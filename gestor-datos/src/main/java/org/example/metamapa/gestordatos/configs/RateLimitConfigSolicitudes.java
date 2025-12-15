package org.example.metamapa.gestordatos.configs;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitConfigSolicitudes {

    @Bean
    public FilterRegistrationBean<RateLimitSolicitudesFilter> rateLimitSolicitudesRegistration() {

        FilterRegistrationBean<RateLimitSolicitudesFilter> registration =
                new FilterRegistrationBean<>();

        registration.setFilter(new RateLimitSolicitudesFilter());

        // Aplicar SOLO al endpoint de solicitudes
        registration.addUrlPatterns("/gestordatos/publica/solicitudes");

        // Ejecutarse antes que el controller
        registration.setOrder(0);

        return registration;
    }
}
