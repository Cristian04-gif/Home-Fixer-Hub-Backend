package com.home_fixer_hub.identity_service.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(csrf -> csrf.disable()) // Deshabilitado para APIs con JWT
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/auth/register", "/auth/login", "/auth/validate/**").permitAll()
                .anyExchange().authenticated()
            )
            //.addFilterAt(customJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION) // Tu filtro de JWT
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // El estándar de la industria
    }
}