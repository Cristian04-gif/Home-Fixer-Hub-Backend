package com.home_fixer_hub.booking_service.Domain.Service;

import com.home_fixer_hub.booking_service.Domain.DTO.BookingDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookingService {

    public Mono<BookingDTO> register(BookingDTO bookingDTO);

    public Mono<BookingDTO> getById(String bookingId);

    public Flux<BookingDTO> getCustomerInquiries(String customerId);

    public Flux<BookingDTO> getQuestionsTheTechnician(String technicalId);

    public Mono<BookingDTO> acceptQuery(String bookingId);

    public Mono<Void> rejectQuery(String bookingId);

    public Mono<Void> cancelQuery(String bookingId);
}
