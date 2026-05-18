package com.home_fixer_hub.profile_service.Domain.Client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class CatalogClient {

    private final WebClient webClient;

    public CatalogClient(WebClient.Builder webClientBuilder,
            @Value("${services.catalog-service.uri}") String identityServiceUrl) {
        this.webClient = webClientBuilder
                .baseUrl(identityServiceUrl)
                .build();
    }

    public Mono<Boolean> deleteRelatedServices(String technicalId) {
        return webClient.delete()
                .uri("/api/catalog/fixer/remove-related-services/{technicalId}")
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .onErrorResume(e -> {
                    System.out.println("Error en eliminacion: " + e.getMessage());
                    return Mono.just(false);
                });
    }

}
