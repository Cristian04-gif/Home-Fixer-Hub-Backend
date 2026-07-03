package com.home_fixer_hub.booking_service.Domain.Service;

import reactor.core.publisher.Mono;

public interface ExpoNotification {
    public Mono<Void> sendPushNotification(String expoPushToken, String title, String body);
}
