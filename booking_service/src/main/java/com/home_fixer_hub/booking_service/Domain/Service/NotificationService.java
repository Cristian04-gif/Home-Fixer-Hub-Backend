package com.home_fixer_hub.booking_service.Domain.Service;
import reactor.core.publisher.Mono;

public interface NotificationService {
    Mono<Void> sendNotificationRequest(String customerId, String technicalId, String bookingStatus, String service);
}
