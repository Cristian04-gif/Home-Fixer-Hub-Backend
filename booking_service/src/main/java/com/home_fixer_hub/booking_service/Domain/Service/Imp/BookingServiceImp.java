package com.home_fixer_hub.booking_service.Domain.Service.Imp;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.home_fixer_hub.booking_service.Domain.Client.CustomerClient;
import com.home_fixer_hub.booking_service.Domain.Client.TechnicalClient;
import com.home_fixer_hub.booking_service.Domain.DTO.BookingDTO;
import com.home_fixer_hub.booking_service.Domain.DTO.CustomerDTO;
import com.home_fixer_hub.booking_service.Domain.DTO.TechnicalDTO;
import com.home_fixer_hub.booking_service.Domain.DTO.Response.BookingResponseTech;
import com.home_fixer_hub.booking_service.Domain.Service.BookingService;
import com.home_fixer_hub.booking_service.Persistense.Mapping.BookingMapper;
import com.home_fixer_hub.booking_service.Persistense.Model.Booking;
import com.home_fixer_hub.booking_service.Persistense.Model.BookingStatus;
import com.home_fixer_hub.booking_service.Persistense.Repository.BookingRepository;
import com.home_fixer_hub.booking_service.Persistense.Utils.LocationUtils;

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
                                ? geocodingService.getAddressFromCoordinates(bookingDTO.latitude(),
                                                bookingDTO.longitude())
                                : Mono.just(bookingDTO.detailedAddress() != null ? bookingDTO.detailedAddress()
                                                : "Direccion sin especificar");

                Mono<CustomerDTO> customerValidator = customerClient.getCustomerId(bookingDTO.customerId())
                                .switchIfEmpty(
                                                Mono.error(new RuntimeException("El cliente con ID "
                                                                + bookingDTO.customerId() + " no existe.")));

                Mono<TechnicalDTO> technicalValidator = technicalClient.getTechnicalById(bookingDTO.technicalId())
                                .switchIfEmpty(Mono
                                                .error(new RuntimeException("El tecnico con ID "
                                                                + bookingDTO.technicalId() + " no existe.")));

                return Mono.zip(customerValidator, technicalValidator, direccionResolver)
                                .flatMap(tuple -> {
                                        String direccionTexto = tuple.getT3();

                                        Booking booking = bookingMapper.toEntity(bookingDTO);
                                        booking.setDireccionDetallada(direccionTexto);
                                        booking.setEstadoConsulta(BookingStatus.PENDIENTE.name());
                                        booking.setFechaConsulta(LocalDateTime.now());
                                        booking.setFechaModificacion(LocalDateTime.now());
                                        return bookingRepository.save(booking);
                                }).map(bookingMapper::toDTO);
        }

        @Override
        public Mono<BookingDTO> getById(String bookingId) {
                return bookingRepository.findById(bookingId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException(
                                                                "No se encontro una solicitud de servicios con el id: "
                                                                                + bookingId)))
                                .map(bookingMapper::toDTO);
        }

        @Override
        public Flux<BookingDTO> getCustomerInquiries(String customerId) {
                return customerClient.getCustomerId(customerId)
                                .flatMapMany(customer -> bookingRepository.findByIdCliente(customer.id()))
                                .filter(bookings -> bookings.getEstadoConsulta().equals(BookingStatus.PENDIENTE.name()))
                                .map(bookingMapper::toDTO);
        }

        @Override
        public Flux<BookingResponseTech> getQuestionsTheTechnician(String technicalId, Double lat1, Double lon1) {

                if (lat1 == null && lon1 == null) {
                        return Flux.error(new IllegalArgumentException(
                                        "No se ingresó la ubicación de consulta del técnico"));
                }

                return technicalClient.getTechnicalById(technicalId)
                                .switchIfEmpty(Mono.error(new RuntimeException(
                                                "No se encontró el técnico con el id " + technicalId)))
                                .flatMapMany(technicalDTO -> bookingRepository.findByIdTecnico(technicalId).filter(
                                                tech -> tech.getEstadoConsulta().equals(BookingStatus.PENDIENTE.name()))
                                                .map(booking -> {

                                                        double distance = LocationUtils.calculateDistance(
                                                                        lat1, lon1,
                                                                        booking.getLatitud(), booking.getLongitud());

                                                        return BookingResponseTech.builder()
                                                                        .id(booking.getId())
                                                                        .serviceType(booking.getTipoServicio())
                                                                        .title(booking.getTitulo())
                                                                        .description(booking.getDescripcion())
                                                                        .inquiryDate(booking.getFechaConsulta())
                                                                        .latitudeCustomer(booking.getLatitud())
                                                                        .longitudeCustomer(booking.getLongitud())
                                                                        .detailedAddress(
                                                                                        booking.getDireccionDetallada())
                                                                        .distanceKm(distance)
                                                                        .totalAmount(booking.getMontoTotal())
                                                                        .inquiryStatus(booking.getEstadoConsulta())
                                                                        .customerId(booking.getIdCliente())
                                                                        .technicalId(technicalId)
                                                                        .build();
                                                }));
        }

        @Override
        public Mono<BookingDTO> acceptQuery(String bookingId) {
                return bookingRepository.findById(bookingId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException(
                                                                "No se encontro la solicitud de servicios con el id: "
                                                                                + bookingId)))
                                .flatMap(booking -> {

                                        booking.setEstadoConsulta(BookingStatus.ACEPTADA.name());
                                        booking.setFechaModificacion(LocalDateTime.now());
                                        return bookingRepository.save(booking);
                                }).map(bookingMapper::toDTO);
        }

        @Override
        public Mono<Void> rejectQuery(String bookingId) {
                return bookingRepository.findById(bookingId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException(
                                                                "No se encontro la solicitud de servicios con el id: "
                                                                                + bookingId)))
                                .flatMap(booking -> {
                                        booking.setEstadoConsulta(BookingStatus.RECHAZADA.name());
                                        booking.setFechaModificacion(LocalDateTime.now());
                                        return bookingRepository.save(booking);
                                }).then();
        }

        @Override
        public Mono<Void> cancelQuery(String bookingId) {
                return bookingRepository.findById(bookingId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException(
                                                                "No se encontro la solicitud de servicios con el id: "
                                                                                + bookingId)))
                                .flatMap(booking -> {
                                        booking.setEstadoConsulta(BookingStatus.CANCELADO.name());
                                        booking.setFechaModificacion(LocalDateTime.now());
                                        return bookingRepository.save(booking);
                                }).then();
        }

}
