package com.home_fixer_hub.booking_service.Web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home_fixer_hub.booking_service.Domain.DTO.BookingDTO;
import com.home_fixer_hub.booking_service.Domain.DTO.Response.BookingResponseTech;
import com.home_fixer_hub.booking_service.Domain.Service.BookingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/bookings")
@Log4j2
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/customer/consult")
    public Mono<ResponseEntity<BookingDTO>> register(@RequestBody BookingDTO bookingDTO) {
        return bookingService.register(bookingDTO).map(value -> ResponseEntity.ok(value)).onErrorResume(e -> {
            log.error("Se produjo un error al registrar una consulta {}", e);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        });
    }

    @GetMapping("/{bookingId}")
    public Mono<ResponseEntity<BookingDTO>> getById(@PathVariable String bookingId) {
        return bookingService.getById(bookingId).map(value -> ResponseEntity.ok(value)).onErrorResume(e -> {
            log.error("No se encontro la consulta {}", e);
            return Mono.just(ResponseEntity.notFound().build());
        });
    }

    @GetMapping("/customer/{customerId}")
    public Flux<ResponseEntity<BookingDTO>> customerinquiries(@PathVariable String customerId) {
        return bookingService.getCustomerInquiries(customerId).map(value -> ResponseEntity.ok(value))
                .onErrorResume(e -> {
                    log.error("No se encontro la consulta {}", e);
                    return Mono.just(ResponseEntity.notFound().build());
                });

    }

    @GetMapping("/fixer/{technicalId}")
    public Flux<BookingResponseTech> queriesForTechnician(@PathVariable String technicalId, @RequestParam Double lat1, @RequestParam Double lon1) {
        return bookingService.getQuestionsTheTechnician(technicalId, lat1, lon1);
    }

    @PutMapping("/{bookingId}/fixer/accept")
    public Mono<ResponseEntity<BookingDTO>> acceptQuery(@PathVariable String bookingId) {
        return bookingService.acceptQuery(bookingId).map(value -> ResponseEntity.ok(value)).onErrorResume(e -> {
            log.error("no se pudo aceptar la consulta {}", e);
            return Mono.just(ResponseEntity.badRequest().build());
        });
    }

    @PutMapping("/{bookingId}/fixer/reject")
    public Mono<Void> rejectQuery(@PathVariable String bookingId) {
        return bookingService.rejectQuery(bookingId);
    }

    @PutMapping("/{bookingId}/reject")
    public Mono<Void> cancelQuery(@PathVariable String bookingId) {
        return bookingService.cancelQuery(bookingId);
    }

}
