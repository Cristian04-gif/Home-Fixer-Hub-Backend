package com.home_fixer_hub.catalog_service.Domain.Service;

import com.home_fixer_hub.catalog_service.Domain.DTO.TechnicalServiceDTO;
import com.home_fixer_hub.catalog_service.Domain.DTO.Response.AllTechnicalDTO;

import reactor.core.publisher.Mono;

public interface TechnicalServiceService {
    public Mono<TechnicalServiceDTO> assignSkill(TechnicalServiceDTO serviceDTO);

    public Mono<TechnicalServiceDTO> getByTecnicalIdAndServiceId(String technicalId, String serviceId);

    public Mono<AllTechnicalDTO> getTechnicalsByService(String serviceId, int pageNumber, int pageSize);

    public Mono<Void> deleteByTechnical(String technicalId);

}
