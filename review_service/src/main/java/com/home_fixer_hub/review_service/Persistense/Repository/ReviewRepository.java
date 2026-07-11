package com.home_fixer_hub.review_service.Persistense.Repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.home_fixer_hub.review_service.Persistense.Models.Review;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface ReviewRepository extends ReactiveCrudRepository<Review, String>{
    public Flux<Review> findByIdTecnico(String idTecnico);
    public Mono<Long> countByIdTecnico(String idTecnico);
}
