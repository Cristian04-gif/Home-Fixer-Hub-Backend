package com.home_fixer_hub.booking_service.Domain.Service.Imp;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.home_fixer_hub.booking_service.Domain.Client.CustomerClient;
import com.home_fixer_hub.booking_service.Domain.Client.TechnicalClient;
import com.home_fixer_hub.booking_service.Domain.DTO.BookingDTO;
import com.home_fixer_hub.booking_service.Domain.DTO.CustomerDTO;
import com.home_fixer_hub.booking_service.Domain.DTO.TechnicalDTO;
import com.home_fixer_hub.booking_service.Domain.Service.BookingService;
import com.home_fixer_hub.booking_service.Persistense.Mapping.BookingMapper;
import com.home_fixer_hub.booking_service.Persistense.Model.Booking;
import com.home_fixer_hub.booking_service.Persistense.Model.BookingStatus;
import com.home_fixer_hub.booking_service.Persistense.Repository.BookingRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BookingServiceImp implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final GeocodingService geocodingService;
    private final CustomerClient customerClient;
    private final TechnicalClient technicalClient;

    @Override
    public Mono<BookingDTO> register(BookingDTO bookingDTO) {
        Mono<String> direccionResolver = (bookingDTO.latitude() != null && bookingDTO.longitude() != null)
                ? geocodingService.getAddressFromCoordinates(bookingDTO.latitude(), bookingDTO.longitude())
                : Mono.just(bookingDTO.detailedAddress() != null ? bookingDTO.detailedAddress()
                        : "Direccion sin especificar");

        Mono<CustomerDTO> customerValidator = customerClient.getCustomerId(bookingDTO.customerId()).switchIfEmpty(
                Mono.error(new RuntimeException("El cliente con ID " + bookingDTO.customerId() + " no existe.")));

        Mono<TechnicalDTO> technicalValidator = technicalClient.getTechnicalById(bookingDTO.technicalId())
                .switchIfEmpty(Mono
                        .error(new RuntimeException("El tecnico con ID " + bookingDTO.technicalId() + " no existe.")));

        return Mono.zip(customerValidator, technicalValidator, direccionResolver)
        .flatMap(tuple->{
            String direccionTexto = tuple.getT3();
            
            Booking booking = bookingMapper.toEntity(bookingDTO);
            booking.setDireccionDetallada(direccionTexto);
            booking.setEstadoConsulta(BookingStatus.PENDIENTE.name());
            booking.setFechaConsulta(LocalDateTime.now());
            return bookingRepository.save(booking);
        }).map(bookingMapper::toDTO);
    }

    @Override
    public Mono<BookingDTO> getById(String bookingId) {
        return bookingRepository.findById(bookingId)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("No se encontro una solicitud de servicios con el id: " + bookingId)))
                .map(bookingMapper::toDTO);
    }

    @Override
    public Flux<BookingDTO> getCustomerInquiries(String customerId) {
        return customerClient.getCustomerId(customerId)
                .flatMapMany(customer -> bookingRepository.findByIdCliente(customer.id()))
                .map(bookingMapper::toDTO);
    }

    @Override
    public Flux<BookingDTO> getQuestionsTheTechnician(String technicalId) {
        return technicalClient.getTechnicalById(technicalId)
                .flatMapMany(technical -> bookingRepository.findByIdTecnico(technical.id())).map(bookingMapper::toDTO);
    }

    @Override
    public Mono<BookingDTO> acceptQuery(String bookingId) {
        return bookingRepository.findById(bookingId)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("No se encontro la solicitud de servicios con el id: " + bookingId)))
                .flatMap(booking -> {
                    booking.setEstadoConsulta(BookingStatus.ACEPTADA.name());
                    return bookingRepository.save(booking);
                }).map(bookingMapper::toDTO);
    }

    @Override
    public Mono<Void> rejectQuery(String bookingId) {
        return bookingRepository.findById(bookingId)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("No se encontro la solicitud de servicios con el id: " + bookingId)))
                .flatMap(booking -> {
                    booking.setEstadoConsulta(BookingStatus.RECHAZADA.name());
                    return bookingRepository.save(booking);
                }).then();
    }

    @Override
    public Mono<Void> cancelQuery(String bookingId) {
        return bookingRepository.findById(bookingId)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("No se encontro la solicitud de servicios con el id: " + bookingId)))
                .flatMap(booking -> {
                    booking.setEstadoConsulta(BookingStatus.CANCELADO.name());
                    return bookingRepository.save(booking);
                }).then();
    }

}
