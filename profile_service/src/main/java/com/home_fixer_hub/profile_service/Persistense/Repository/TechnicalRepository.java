package com.home_fixer_hub.profile_service.Persistense.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.home_fixer_hub.profile_service.Persistense.Model.Technical;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TechnicalRepository extends ReactiveCrudRepository<Technical, String> {
    Flux<Technical> findAllBy(Pageable pageable);

    Flux<Technical> findAllByDisponibleTrue(Pageable pageable);

    Mono<Technical> findByIdUsuario(String idUsuario);
}
