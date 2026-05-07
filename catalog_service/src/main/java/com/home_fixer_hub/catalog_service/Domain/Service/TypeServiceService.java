package com.home_fixer_hub.catalog_service.Domain.Service;

import com.home_fixer_hub.catalog_service.Domain.DTO.TypeServiceDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TypeServiceService {

    public Flux<TypeServiceDTO> getAll();

    public Mono<TypeServiceDTO> getById(String id);

    public Mono<TypeServiceDTO> register(TypeServiceDTO typeServiceDTO);
}
