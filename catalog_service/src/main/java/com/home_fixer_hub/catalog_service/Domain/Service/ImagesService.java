package com.home_fixer_hub.catalog_service.Domain.Service;

import org.springframework.http.codec.multipart.FilePart;

import com.home_fixer_hub.catalog_service.Domain.DTO.ImagesDTO;

import reactor.core.publisher.Flux;

public interface ImagesService {
    public Flux<ImagesDTO> getImagesByTechnicalServiceId(String id);

    public Flux<ImagesDTO> assignImagesToTechnicianAndServiceRelationships(String technicalServiceId,
            Flux<FilePart> filePartFlux);

}
