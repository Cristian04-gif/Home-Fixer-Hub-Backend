package com.home_fixer_hub.catalog_service.Persitense.Repository;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.home_fixer_hub.catalog_service.Persitense.Model.Images;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ImagesRepository extends ReactiveCrudRepository<Images, String> {
    Flux<Images> findAllByIdTecnicoServicio(String technicalServiceId);
    Mono<Void> deleteAllByIdTecnicoServicio(String id);
}
