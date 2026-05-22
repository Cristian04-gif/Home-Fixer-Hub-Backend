package com.home_fixer_hub.catalog_service.Domain.Service;

import com.home_fixer_hub.catalog_service.Domain.DTO.ImagesDTO;

import reactor.core.publisher.Flux;

public interface ImagesService {
    public Flux<ImagesDTO> getImagesByTechnicalServiceId(String technicalId, String serviceId);

}
