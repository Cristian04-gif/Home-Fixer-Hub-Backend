package com.home_fixer_hub.catalog_service.Domain.Service;

import com.home_fixer_hub.catalog_service.Domain.DTO.TypeServiceDTO;
import com.home_fixer_hub.catalog_service.Domain.DTO.Response.TypeServiceResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TypeServiceService {

    public Flux<TypeServiceResponse> getAll();

    public Mono<TypeServiceDTO> getById(String id);

    public Mono<TypeServiceDTO> register(TypeServiceDTO typeServiceDTO);

    public Mono<TypeServiceDTO> update(String serviceId, TypeServiceDTO serviceDTO);

    public Mono<Void> deleteById(String serviceId);

}
