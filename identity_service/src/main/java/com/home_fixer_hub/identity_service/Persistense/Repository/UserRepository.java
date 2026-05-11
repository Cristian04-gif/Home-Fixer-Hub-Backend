package com.home_fixer_hub.identity_service.Persistense.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.home_fixer_hub.identity_service.Persistense.Model.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, String> {
    Flux<User> findAllBy(Pageable pageable);
    Mono<Boolean> existsById(String userId);
    Mono<User> findByEmail(String email);

    Mono<Boolean> existsByEmail(String email);
}
