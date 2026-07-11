package com.home_fixer_hub.booking_service.Domain.Service;
import reactor.core.publisher.Mono;

public interface NotificationService {
    Mono<Void> sendNotificationRequest(String customerId, String technicalId, String bookingStatus, String service);

    Mono<Void> sendNotificationRequestFinishBooking(String customerId, String technicalId, String service, String bookingId);
}
