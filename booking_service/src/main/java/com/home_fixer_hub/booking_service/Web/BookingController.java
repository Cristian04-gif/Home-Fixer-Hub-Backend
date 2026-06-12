package com.home_fixer_hub.booking_service.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home_fixer_hub.booking_service.Domain.DTO.BookingDTO;
import com.home_fixer_hub.booking_service.Domain.Service.BookingService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/bookings")
@Log4j2
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/customer/consult")
    public Mono<ResponseEntity<BookingDTO>> register(@RequestBody BookingDTO bookingDTO) {
        return bookingService.register(bookingDTO).map(value -> ResponseEntity.ok(value)).onErrorResume(e -> {
            log.error("Se produjo un error al registrar una consulta {}", e);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        });
    }

}
