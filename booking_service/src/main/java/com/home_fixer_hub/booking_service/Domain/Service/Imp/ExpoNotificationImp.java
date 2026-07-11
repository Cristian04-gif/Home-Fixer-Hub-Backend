package com.home_fixer_hub.booking_service.Domain.Service.Imp;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.home_fixer_hub.booking_service.Domain.Service.ExpoNotification;

import reactor.core.publisher.Mono;

@Service
public class ExpoNotificationImp implements ExpoNotification {

        private final WebClient webClient;

        public ExpoNotificationImp() {
                this.webClient = WebClient.builder()
                                .baseUrl("https://exp.host/--/api/v2")
                                .build();
        }

        @Override
        public Mono<Void> sendPushNotification(String expoPushToken, String title, String body) {
                Map<String, Object> requestBody = Map.of(
                                "to", expoPushToken,
                                "title", title,
                                "body", body,
                                "sound", "default");

                return this.webClient.post()
                                .uri("/push/send")
                                .header("Content-Type", "application/json")
                                .bodyValue(requestBody)
                                .retrieve()

                                .bodyToMono(String.class)
                                .doOnNext(response -> System.out.println("Respuesta de Expo: " + response))
                                .doOnError(error -> System.err
                                                .println("Error enviando la notificación: " + error.getMessage()))
                                .then();
        }

        @Override
        public Mono<Void> sendPushNotification(String expoPushToken, String title, String body, String bookingId,
                        String technicalId) {
                Map<String, Object> dataPayload = Map.of(
                                "screen", "RateService",
                                "bookingId", bookingId,
                                "technicalId", technicalId);

                Map<String, Object> requestBody = Map.of(
                                "to", expoPushToken,
                                "title", title,
                                "body", body,
                                "sound", "default",
                                "data", dataPayload);
                
                

                return this.webClient.post()
                                .uri("/push/send")
                                .header("Content-Type", "application/json")
                                .bodyValue(requestBody)
                                .retrieve()
                                .bodyToMono(String.class)
                                .doOnNext(response -> System.out.println("Respuesta de Expo: " + response))
                                .doOnError(error -> System.err
                                                .println("Error enviando la notificación: " + error.getMessage()))
                                .then();

        }
}