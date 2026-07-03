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
            .uri("/push/send") // Ahora sí concatena: https://exp.host/--/api/v2/push/send
            .header("Content-Type", "application/json")
            .bodyValue(requestBody)
            .retrieve()
            // Cambiamos a String temporalmente para que puedas imprimir la respuesta exacta de Expo si falla
            .bodyToMono(String.class) 
            .doOnNext(response -> System.out.println("Respuesta de Expo: " + response))
            .doOnError(error -> System.err.println("Error enviando la notificación: " + error.getMessage()))
            .then(); // Volvemos a transformarlo en Mono<Void> para cumplir con tu interfaz
    }

}
