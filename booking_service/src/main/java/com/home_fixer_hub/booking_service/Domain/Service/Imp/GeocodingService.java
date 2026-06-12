package com.home_fixer_hub.booking_service.Domain.Service.Imp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
public class GeocodingService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper; // Manejador manual de JSON

    @Value("${google.maps.api-key}")
    private String apiKey;

    public GeocodingService() {
        this.objectMapper = new ObjectMapper(); // Inicializa el mapper compatible
        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().secure()))
                .baseUrl("https://maps.googleapis.com")
                .build();
    }

    public Mono<String> getAddressFromCoordinates(Double latitud, Double longitud) {
        String latlng = latitud + "," + longitud;

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/maps/api/geocode/json")
                        .queryParam("latlng", latlng)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class) 
                .map(jsonString -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(jsonString);

                        boolean statusOk = jsonNode.has("status") && "OK".equals(jsonNode.get("status").asText());
                        boolean hasResults = jsonNode.has("results") && jsonNode.get("results").isArray() && !jsonNode.get("results").isEmpty();

                        if (statusOk && hasResults) {
                            return jsonNode.get("results").get(0).get("formatted_address").asText();
                        }
                        
                        String apiStatus = jsonNode.has("status") ? jsonNode.get("status").asText() : "UNKNOWN_ERROR";
                        return "Dirección no encontrada (Status Google: " + apiStatus + ")";
                        
                    } catch (Exception e) {
                        System.err.println("🚨 Error al procesar el JSON de Google: " + e.getMessage());
                        return "Error al interpretar la dirección geográfica";
                    }
                })
                .doOnError(error -> System.err.println("🚨 ERROR CRÍTICO EN LA PETICIÓN: " + error.getMessage()))
                // Devolvemos un texto seguro si la red llegara a caer para que no rompa el Mono.zip
                .onErrorReturn("Servicio de mapas temporalmente no disponible");
    }
}
