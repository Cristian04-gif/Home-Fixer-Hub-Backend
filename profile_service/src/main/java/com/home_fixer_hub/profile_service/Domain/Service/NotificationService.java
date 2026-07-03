package com.home_fixer_hub.profile_service.Domain.Service;

import reactor.core.publisher.Mono;

public interface NotificationService {
    public Mono<Void> savePushToken(String id, String pushToken, String typeUser);
}
