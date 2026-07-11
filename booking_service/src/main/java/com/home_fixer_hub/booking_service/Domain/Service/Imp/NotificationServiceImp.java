package com.home_fixer_hub.booking_service.Domain.Service.Imp;

import org.springframework.stereotype.Service;

import com.home_fixer_hub.booking_service.Domain.Client.CustomerClient;
import com.home_fixer_hub.booking_service.Domain.Client.TechnicalClient;
import com.home_fixer_hub.booking_service.Domain.DTO.CustomerDTO;
import com.home_fixer_hub.booking_service.Domain.DTO.TechnicalDTO;
import com.home_fixer_hub.booking_service.Domain.Service.ExpoNotification;
import com.home_fixer_hub.booking_service.Domain.Service.NotificationService;
import com.home_fixer_hub.booking_service.Persistense.Model.Booking;
import com.home_fixer_hub.booking_service.Persistense.Model.BookingStatus;
import com.home_fixer_hub.booking_service.Persistense.Repository.BookingRepository;
import com.home_fixer_hub.booking_service.Persistense.Utils.NotificationBody;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotificationServiceImp implements NotificationService {

    private final TechnicalClient technicalClient;
    private final CustomerClient customerClient;
    private final ExpoNotification expoNotification;
    private final BookingRepository bookingRepository;

    @Override
    public Mono<Void> sendNotificationRequest(String customerId, String technicalId, String bookingStatus,
            String service) {
        Mono<TechnicalDTO> techValidator = technicalClient.getTechnicalById(technicalId)
                .switchIfEmpty(Mono.error(new RuntimeException("NO se encontro el tecnico " + technicalId)));
        Mono<CustomerDTO> customerValidator = customerClient.getCustomerId(customerId)
                .switchIfEmpty(Mono.error(new RuntimeException("NO se encontro el cliente " + customerId)));

        return Mono.zip(techValidator, customerValidator).flatMap(tuple -> {
            TechnicalDTO tech = tuple.getT1();
            CustomerDTO cliente = tuple.getT2();

            String pushTokenDestino = "";
            String title = "";
            String body = "";

            if (bookingStatus.equals(BookingStatus.PENDIENTE.name())) {
                pushTokenDestino = tech.pushToken();
                title = NotificationBody.TITLE_NEW_REQUEST;
                body = NotificationBody.BODY_NEW_REQUEST + cliente.name();
            }
            ;
            if (bookingStatus.equals(BookingStatus.ACEPTADA.name())) {
                pushTokenDestino = cliente.pushToken();
                title = NotificationBody.TITLE_REQUEST_ACEPT;
                body = tech.name() + NotificationBody.BODY_REQUEST_ACEPT + service;
            }
            ;
            if (bookingStatus.equals(BookingStatus.RECHAZADA.name())) {
                pushTokenDestino = cliente.pushToken();
                title = NotificationBody.TITLE_REQUEST_REJECT;
                body = tech.name() + NotificationBody.BODY_REQUEST_REJECT + service;
            }
            ;
            if (bookingStatus.equals(BookingStatus.EN_PROCESO.name())) {
                pushTokenDestino = cliente.pushToken();
                title = NotificationBody.TITLE_REQUEST_PROCESS;
                body = NotificationBody.BODY_REQUEST_PROCESS;
            }
            ;
            if (bookingStatus.equals(BookingStatus.CANCELADO.name())) {
                pushTokenDestino = cliente.pushToken();
                title = NotificationBody.TITLE_REQUEST_CANCELLED;
                body = NotificationBody.BODY_REQUEST_CANCELLED;
            }
            ;

            return expoNotification.sendPushNotification(pushTokenDestino, title, body);
        });

    }

    @Override
    public Mono<Void> sendNotificationRequestFinishBooking(String customerId, String technicalId,
            String service, String bookingId) {
        Mono<TechnicalDTO> techValidator = technicalClient.getTechnicalById(technicalId)
                .switchIfEmpty(Mono.error(new RuntimeException("NO se encontro el tecnico " + technicalId)));
        Mono<CustomerDTO> customerValidator = customerClient.getCustomerId(customerId)
                .switchIfEmpty(Mono.error(new RuntimeException("NO se encontro el cliente " + customerId)));

        Mono<Booking> bookingvalidator = bookingRepository.findById(bookingId)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontro el servicio")));

        return Mono.zip(techValidator, customerValidator, bookingvalidator).flatMap(tuple -> {
            TechnicalDTO tech = tuple.getT1();
            CustomerDTO cliente = tuple.getT2();

            String pushTokenDestino = cliente.pushToken();
            String title = NotificationBody.TITLE_REQUEST_COMPLETED;
            String body = NotificationBody.BODY_REQUEST_COMPLETED;

            return expoNotification.sendPushNotification(pushTokenDestino, title, body, bookingId, tech.id());
        });

    }

}
