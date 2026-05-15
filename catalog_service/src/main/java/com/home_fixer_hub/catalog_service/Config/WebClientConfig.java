package com.home_fixer_hub.catalog_service.Config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    @LoadBalanced // Permite usar el nombre del servicio en Eureka en lugar de la IP
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
