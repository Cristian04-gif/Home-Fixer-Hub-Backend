package com.home_fixer_hub.booking_service.Domain.Service;

import com.home_fixer_hub.booking_service.Domain.DTO.BookingDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WorkService {
    public Mono<BookingDTO> getWork(String bookingId);

    public Flux<BookingDTO> getJobsCustomer(String customerId);

    public Flux<BookingDTO> getJobsTechnical(String technicalId);

    public Mono<BookingDTO> startWork(String bookingId);

    public Mono<BookingDTO> finishWork(String bookingId);

    public Mono<BookingDTO> cancelWork(String bookingId);

}
