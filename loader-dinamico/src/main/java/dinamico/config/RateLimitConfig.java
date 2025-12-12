package dinamico.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitConfig {

    @Bean
    public FilterRegistrationBean<RateLimitUsuarioFilter> rateLimitFilterRegistration() {
        FilterRegistrationBean<RateLimitUsuarioFilter> registration = new FilterRegistrationBean<>();

        registration.setFilter(new RateLimitUsuarioFilter());


        registration.addUrlPatterns("/fuenteDinamica/hecho");

        registration.setOrder(0); // que se ejecute antes del multipart resolver
        return registration;
    }
}