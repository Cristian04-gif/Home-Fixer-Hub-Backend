package com.home_fixer_hub.profile_service.Domain.Client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.home_fixer_hub.profile_service.Domain.DTO.BookingDTO;

import reactor.core.publisher.Mono;

@Component
public class BookingClient {

    private final WebClient webClient;

    public BookingClient(WebClient.Builder webClient,
            @Value("${services.booking-service.url}") String bookingServiceUrl) {
        this.webClient = webClient.baseUrl(bookingServiceUrl).build();
    }

    public Mono<BookingDTO> getJobById(String bookingId) {
        return webClient.get()
                .uri("/api/bookings/job/{bookingId}", bookingId)
                .retrieve()
                .bodyToMono(BookingDTO.class);
    }

}
