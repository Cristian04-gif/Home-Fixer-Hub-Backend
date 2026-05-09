package com.home_fixer_hub.profile_service.Domain.Service;

import com.home_fixer_hub.profile_service.Domain.DTO.TechnicalDTO;
import com.home_fixer_hub.profile_service.Domain.DTO.Response.AllTechnicalDTO;

import reactor.core.publisher.Mono;

public interface TechnicalService {

    public Mono<AllTechnicalDTO> getAll(int page, int size);

    public Mono<TechnicalDTO> getbyId(String technicalid);

    public Mono<TechnicalDTO> register(TechnicalDTO technicalDTO);
}
