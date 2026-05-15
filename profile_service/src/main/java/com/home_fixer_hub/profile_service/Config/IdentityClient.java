package com.home_fixer_hub.profile_service.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class IdentityClient {

    private final WebClient webClient;

    // Inyectamos el valor directamente en el parámetro del constructor
    public IdentityClient(WebClient.Builder webClientBuilder, 
                          @Value("${services.identity-service.url}") String identityServiceUrl) {
        this.webClient = webClientBuilder
                .baseUrl(identityServiceUrl) // Ahora sí tiene valor: http://IDENTITY-SERVICE
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
