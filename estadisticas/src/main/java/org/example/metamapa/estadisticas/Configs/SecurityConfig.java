package org.example.metamapa.estadisticas.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AdminOnlyJwtFilter adminFilter;

    public SecurityConfig(AdminOnlyJwtFilter adminFilter) {
        this.adminFilter = adminFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/estadisticas/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .addFilterBefore(adminFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
