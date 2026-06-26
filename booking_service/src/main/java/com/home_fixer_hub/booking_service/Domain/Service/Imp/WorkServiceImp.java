package com.home_fixer_hub.booking_service.Domain.Service.Imp;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.home_fixer_hub.booking_service.Domain.Client.CustomerClient;
import com.home_fixer_hub.booking_service.Domain.Client.TechnicalClient;
import com.home_fixer_hub.booking_service.Domain.DTO.BookingDTO;
import com.home_fixer_hub.booking_service.Domain.Service.WorkService;
import com.home_fixer_hub.booking_service.Persistense.Mapping.BookingMapper;
import com.home_fixer_hub.booking_service.Persistense.Model.BookingStatus;
import com.home_fixer_hub.booking_service.Persistense.Repository.BookingRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WorkServiceImp implements WorkService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CustomerClient customerClient;
    private final TechnicalClient technicalClient;

    @Override
    public Mono<BookingDTO> getWork(String bookingId) {
        return bookingRepository.findById(bookingId)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontro el trabajo con el id: " + bookingId)))
                .map(bookingMapper::toDTO);
    }

    @Override
    public Flux<BookingDTO> getJobsCustomer(String customerId) {
        return customerClient.getCustomerId(customerId)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontro el cliente: " + customerId)))
                .flatMapMany(customer -> bookingRepository.findByIdCliente(customerId)).map(bookingMapper::toDTO);
    }

    @Override
    public Flux<BookingDTO> getJobsTechnical(String technicalId) {
        return technicalClient.getTechnicalById(technicalId)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontro el tecnico: " + technicalId)))
                .flatMapMany(customer -> bookingRepository.findByIdTecnico(technicalId)).map(bookingMapper::toDTO);
    }

    @Override
    public Mono<BookingDTO> startWork(String bookingId) {
        return bookingRepository.findById(bookingId)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontro un trabajo con el id: " + bookingId)))
                .flatMap(booking -> {
                    booking.setEstadoConsulta(BookingStatus.EN_PROCESO.name());
                    booking.setFechaModificacion(LocalDateTime.now());
                    return bookingRepository.save(booking);
                }).map(bookingMapper::toDTO);
    }

    @Override
    public Mono<BookingDTO> finishWork(String bookingId) {
        return bookingRepository.findById(bookingId)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontro un trabajo con el id: " + bookingId)))
                .flatMap(booking -> {
                    booking.setEstadoConsulta(BookingStatus.FINALIZADA.name());
                    booking.setFechaModificacion(LocalDateTime.now());
                    return bookingRepository.save(booking);
                }).map(bookingMapper::toDTO);
    }

    @Override
    public Mono<BookingDTO> cancelWork(String bookingId) {
        return bookingRepository.findById(bookingId)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontro un trabajo con el id: " + bookingId)))
                .flatMap(booking -> {
                    booking.setEstadoConsulta(BookingStatus.CANCELADO.name());
                    booking.setFechaModificacion(LocalDateTime.now());
                    return bookingRepository.save(booking);
                }).map(bookingMapper::toDTO);
    }

}
