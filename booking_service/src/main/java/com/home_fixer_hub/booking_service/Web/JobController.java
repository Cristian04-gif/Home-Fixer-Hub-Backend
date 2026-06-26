package com.home_fixer_hub.booking_service.Web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home_fixer_hub.booking_service.Domain.DTO.BookingDTO;
import com.home_fixer_hub.booking_service.Domain.Service.WorkService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Log4j2
public class JobController {

    private final WorkService workService;

    @GetMapping("/job/{bookingId}")
    public Mono<ResponseEntity<BookingDTO>> getWork(@PathVariable String bookingId) {
        return workService.getWork(bookingId).map(value -> ResponseEntity.ok(value)).onErrorResume(e -> {
            log.error("No se encontro el trabajo con el id {}", e);
            return Mono.just(ResponseEntity.notFound().build());
        });
    }

    @GetMapping("/jobs/customer/{customerId}")
    public Flux<BookingDTO> getJobsCustomer(@PathVariable String customerId) {
        return workService.getJobsCustomer(customerId);
    }

    @GetMapping("/jobs/fixer/{technicalId}")
    public Flux<BookingDTO> getJobsTechnical(@PathVariable String technicalId) {
        return workService.getJobsTechnical(technicalId);
    }

    @PutMapping("/job/{bookingId}/start")
    public Mono<ResponseEntity<BookingDTO>> startWork(@PathVariable String bookingId) {
        return workService.startWork(bookingId).map(value -> ResponseEntity.ok(value)).onErrorResume(e -> {
            log.error("NO se pudo empezar con el trabajo {}", e);
            return Mono.just(ResponseEntity.internalServerError().build());
        });
    }

    @PutMapping("/job/{bookingId}/finish")
    public Mono<ResponseEntity<BookingDTO>> finishWork(@PathVariable String bookingId) {
        return workService.finishWork(bookingId).map(value -> ResponseEntity.ok(value)).onErrorResume(e -> {
            log.error("NO se pudo finalizar con el trabajo {}", e);
            return Mono.just(ResponseEntity.internalServerError().build());
        });
    }

    @PutMapping("/job/{bookingId}/cancel")
    public Mono<ResponseEntity<BookingDTO>> cancelWork(@PathVariable String bookingId) {
        return workService.cancelWork(bookingId).map(value -> ResponseEntity.ok(value)).onErrorResume(e -> {
            log.error("NO se pudo finalizar con el trabajo {}", e);
            return Mono.just(ResponseEntity.internalServerError().build());
        });
    }

}
