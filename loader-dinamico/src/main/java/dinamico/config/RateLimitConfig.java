package dinamico.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RateLimitConfig {

    @Bean
    public FilterRegistrationBean<RateLimitUsuarioFilter> rateLimitFilterRegistration() {

        FilterRegistrationBean<RateLimitUsuarioFilter> registration =
                new FilterRegistrationBean<>();

        registration.setFilter(new RateLimitUsuarioFilter());

        // Endpoint a proteger
        registration.addUrlPatterns("/fuenteDinamica/hecho");

        // Que se ejecute antes del multipart resolver
        registration.setOrder(0);

        return registration;
    }
}