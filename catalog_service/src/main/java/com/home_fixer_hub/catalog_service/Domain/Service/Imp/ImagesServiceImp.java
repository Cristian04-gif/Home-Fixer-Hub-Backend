package com.home_fixer_hub.catalog_service.Domain.Service.Imp;

import java.time.LocalDate;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import com.home_fixer_hub.catalog_service.Domain.DTO.ImagesDTO;
import com.home_fixer_hub.catalog_service.Domain.Service.CloudinaryService;
import com.home_fixer_hub.catalog_service.Domain.Service.ImagesService;
import com.home_fixer_hub.catalog_service.Persitense.Mapping.ImagesMapper;
import com.home_fixer_hub.catalog_service.Persitense.Model.Images;
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

    private final CloudinaryService cloudinaryService;

    @Override
    public Flux<ImagesDTO> getImagesByTechnicalServiceId(String id) {
        return imagesRepository.findAllByIdTecnicoServicio(id)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontro las imagenes de esta relacion")))
                .map(imagesMapper::toDTO);
    }

    @Override
    public Flux<ImagesDTO> assignImagesToTechnicianAndServiceRelationships(String technicalServiceId,
            Flux<FilePart> filePartFlux) {
        return repository.findById(technicalServiceId).switchIfEmpty(Mono.error(new RuntimeException(
                "No se encontor la informacion relacionado con el tecnico y servicio " + technicalServiceId)))
                .flatMapMany(
                        value -> filePartFlux.flatMap(cloudinaryService::uploadImageCloud).flatMap(secureUrl -> {
                            Images images = Images.builder()
                                    .url(secureUrl)
                                    .fechaRegistro(LocalDate.now())
                                    .idTecnicoServicio(technicalServiceId)
                                    .build();
                            return imagesRepository.save(images).map(imagesMapper::toDTO);
                        }));
    }

}
