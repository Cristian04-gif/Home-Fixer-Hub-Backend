package com.home_fixer_hub.review_service.Domain.Client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.home_fixer_hub.review_service.Domain.DTO.TechnicalDTO;

import reactor.core.publisher.Mono;

@Component
public class TechnicalClient {

    private final WebClient webClient;

    public TechnicalClient(WebClient.Builder webClient,
            @Value("${services.profile-service.url}") String technicalServiceUrl) {
        this.webClient = webClient.baseUrl(technicalServiceUrl).build();
    }

    public Mono<TechnicalDTO> getTechnicalById(String technicalId) {
        return webClient.get()
                .uri("/api/profile/technicals/{technicalId}", technicalId)
                .retrieve()
                .bodyToMono(TechnicalDTO.class);
    }

    public Mono<Void> saveRatingTechnical(String technicalId, String bookingId, Double promedio) {

        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("technicalId", technicalId);
        uriVariables.put("bookingId", bookingId);
        uriVariables.put("promedio", promedio);

        return webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/profile/technicals/saveRating/{technicalId}")
                        .queryParam("bookingId", "{bookingId}")
                        .queryParam("promedio", "{promedio}")
                        .build(uriVariables))
                .retrieve()
                .bodyToMono(Void.class);
    }

}
