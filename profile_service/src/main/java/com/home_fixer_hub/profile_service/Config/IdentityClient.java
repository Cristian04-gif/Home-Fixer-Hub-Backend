package com.home_fixer_hub.profile_service.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
public class IdentityClient {

    private final WebClient webClient;

    @Value("${services.identity-service.url}")
    private String identityServiceUrl;

    public IdentityClient(WebClient.Builder webClient) {
        this.webClient = webClient.baseUrl(identityServiceUrl).build();
    }

    public Mono<Boolean> isValidUser(String userId) {
        return webClient.get()
                .uri("/auth/validate/{userId}", userId)
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .onErrorReturn(false);

    }
}
