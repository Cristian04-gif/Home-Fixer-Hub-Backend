package com.home_fixer_hub.booking_service.Domain.Client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.home_fixer_hub.booking_service.Domain.DTO.CustomerDTO;

import reactor.core.publisher.Mono;

@Component
public class CustomerClient {

    private final WebClient webClient;

    public CustomerClient(WebClient.Builder webClient,
            @Value("${services.profile-service.url}") String customerServiceUrl) {
        this.webClient = webClient.baseUrl(customerServiceUrl).build();
    }

    public Mono<CustomerDTO> getCustomerId(String customerId) {
        return webClient.get()
                .uri("/api/profile/customers/{customerId}", customerId)
                .retrieve()
                .bodyToMono(CustomerDTO.class);
    }
}
