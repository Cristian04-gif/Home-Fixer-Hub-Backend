package com.home_fixer_hub.catalog_service.Persitense.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.home_fixer_hub.catalog_service.Persitense.Model.TechnicalService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TechnicalServiceRepository extends ReactiveCrudRepository<TechnicalService, String> {
    Flux<TechnicalService> findAllByIdTecnico(String idTecnico);

    Flux<TechnicalService> findAllByIdServicio(String idServicio, Pageable pageable);

    Mono<TechnicalService> findByIdTecnicoAndIdServicio(String idTecnico, String idServicio);

    Mono<Long> countByIdServicio(String idServicio);

    Mono<Void> deleteAllByIdServicio(String idService);

    Mono<Void> deleteAllByIdTecnico(String idTecnico);
}
