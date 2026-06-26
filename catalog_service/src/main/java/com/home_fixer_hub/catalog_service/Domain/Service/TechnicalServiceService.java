package com.home_fixer_hub.catalog_service.Domain.Service;

import com.home_fixer_hub.catalog_service.Domain.DTO.TechnicalDTO;
import com.home_fixer_hub.catalog_service.Domain.DTO.TechnicalServiceDTO;
import com.home_fixer_hub.catalog_service.Domain.DTO.Response.TechnicalSkills;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TechnicalServiceService {
    public Mono<TechnicalServiceDTO> assignSkill(TechnicalServiceDTO serviceDTO);

    public Flux<TechnicalServiceDTO> getByTecnicalIdAndServiceId(String technicalId, String serviceId);

    public Flux<TechnicalDTO> getTechnicalsByService(String serviceId);

    public Mono<Void> deleteByTechnical(String technicalId);

    public Flux<TechnicalSkills> getTechnicianServices(String technicalId);

    public Mono<Void> removeSkill(String technicalServiceId);

}
