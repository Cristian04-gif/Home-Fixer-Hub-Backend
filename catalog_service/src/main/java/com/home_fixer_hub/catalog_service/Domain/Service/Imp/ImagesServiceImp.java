package com.home_fixer_hub.catalog_service.Domain.Service.Imp;

import org.springframework.stereotype.Service;

import com.home_fixer_hub.catalog_service.Domain.DTO.ImagesDTO;
import com.home_fixer_hub.catalog_service.Domain.Service.ImagesService;
import com.home_fixer_hub.catalog_service.Persitense.Mapping.ImagesMapper;
import com.home_fixer_hub.catalog_service.Persitense.Repository.ImagesRepository;
import com.home_fixer_hub.catalog_service.Persitense.Repository.TechnicalServiceRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ImagesServiceImp implements ImagesService {

    private final TechnicalServiceRepository repository;
    private final ImagesRepository imagesRepository;
    private final ImagesMapper imagesMapper;

    @Override
    public Flux<ImagesDTO> getImagesByTechnicalServiceId(String technicalId, String serviceId) {
        return repository.findAllByIdTecnico(technicalId)
                .filter(value -> value.getIdServicio().equalsIgnoreCase(serviceId))
                .flatMap(value -> imagesRepository.findAllByIdTecnicoServicio(value.getId()))
                .map(imagesMapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException(
                        "No se encontro las images relacionados con el tenico de id "
                                + technicalId)));
    }

}
