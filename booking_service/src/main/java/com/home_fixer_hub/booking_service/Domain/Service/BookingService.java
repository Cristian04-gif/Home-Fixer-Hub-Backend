package com.home_fixer_hub.booking_service.Domain.Service;

import com.home_fixer_hub.booking_service.Domain.DTO.BookingDTO;
import com.home_fixer_hub.booking_service.Domain.DTO.Response.BookingResponseTech;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookingService {

    public Mono<BookingDTO> register(BookingDTO bookingDTO);

    public Mono<BookingDTO> getById(String bookingId);

    public Flux<BookingDTO> getCustomerInquiries(String customerId);

    public Flux<BookingResponseTech> getQuestionsTheTechnician(String technicalId, Double lat1, Double lon1);

    public Mono<BookingDTO> acceptQuery(String bookingId);

    public Mono<Void> rejectQuery(String bookingId);

    public Mono<Void> cancelQuery(String bookingId);
}
