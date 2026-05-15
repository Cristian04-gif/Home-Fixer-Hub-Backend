package com.home_fixer_hub.catalog_service.Domain.Client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.home_fixer_hub.catalog_service.Domain.DTO.TechnicalDTO;

import reactor.core.publisher.Mono;

@Component
public class ProfileClient {

    private final WebClient webClient;

    @Value("${services.profile-service.url}")
    private String technicalServiceUrl;

    public ProfileClient(WebClient.Builder webClient,
            @Value("${services.profile-service.url}") String technicalServiceUrl) {
        this.webClient = webClient.baseUrl(technicalServiceUrl).build();
    }

    public Mono<TechnicalDTO> getTechnicalById(String technicalId) {
        return webClient.get()
                .uri("/api/profile/technicals/{technicalId}", technicalId)
                .retrieve()
                .bodyToMono(TechnicalDTO.class);

    }
}
