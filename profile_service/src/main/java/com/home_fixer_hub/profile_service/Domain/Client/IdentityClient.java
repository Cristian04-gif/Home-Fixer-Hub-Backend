package com.home_fixer_hub.profile_service.Domain.Client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class IdentityClient {

    private final WebClient webClient;

    public IdentityClient(WebClient.Builder webClientBuilder, 
                          @Value("${services.identity-service.url}") String identityServiceUrl) {
        this.webClient = webClientBuilder
                .baseUrl(identityServiceUrl) 
                .build();
    }

    public Mono<Boolean> isValidUser(String userId) {
        return webClient.get()
                .uri("/auth/validate/{userId}", userId)
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .onErrorResume(e -> {
                System.out.println("Error en validación: " + e.getMessage());
                return Mono.just(false);
            });

    }
}
