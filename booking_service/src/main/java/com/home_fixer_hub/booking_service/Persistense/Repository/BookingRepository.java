package com.home_fixer_hub.booking_service.Persistense.Repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.home_fixer_hub.booking_service.Persistense.Model.Booking;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BookingRepository extends ReactiveCrudRepository<Booking, String> {
    Flux<Booking> findByIdCliente(String idCliente);

    Flux<Booking> findByIdTecnico(String idTecnico);

    Mono<Long> countByIdTecnicoAndEstadoConsulta(String idTecnico, String estadoConsulta);
}
