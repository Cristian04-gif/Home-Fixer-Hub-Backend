package com.home_fixer_hub.profile_service.Domain.Service.Imp;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import com.home_fixer_hub.profile_service.Domain.Client.CatalogClient;
import com.home_fixer_hub.profile_service.Domain.Client.IdentityClient;
import com.home_fixer_hub.profile_service.Domain.DTO.TechnicalDTO;
import com.home_fixer_hub.profile_service.Domain.DTO.Response.AllTechnicalDTO;
import com.home_fixer_hub.profile_service.Domain.Service.CloudinaryService;
import com.home_fixer_hub.profile_service.Domain.Service.TechnicalService;
import com.home_fixer_hub.profile_service.Persistense.Mapping.TechnicalMapper;
import com.home_fixer_hub.profile_service.Persistense.Model.Technical;
import com.home_fixer_hub.profile_service.Persistense.Repository.TechnicalRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TechnicalServiceImp implements TechnicalService {

    private final TechnicalRepository technicalRepository;
    private final TechnicalMapper technicalMapper;
    private final IdentityClient identityClient;
    private final CatalogClient catalogClient;
    private final CloudinaryService cloudinaryService;

    @Override
    public Mono<AllTechnicalDTO> getAll(int page, int size) {

        Mono<List<TechnicalDTO>> technicals = technicalRepository
                .findAllBy(PageRequest.of(page, size, Sort.by("id").ascending())).map(technicalMapper::toDTO)
                .collectList();

        Mono<Long> count = technicalRepository.count();

        return Mono.zip(technicals, count).map(tuple -> {
            List<TechnicalDTO> list = tuple.getT1();
            long totalElements = tuple.getT2();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            boolean last = page >= totalPages - 1;
            return AllTechnicalDTO.builder()
                    .technicalDTOs(list)
                    .pageNumber(page)
                    .pageSize(totalElements > 0 ? size : 0)
                    .totalElements(totalElements)
                    .totalPages(totalPages)
                    .last(last)
                    .build();
        });

    }

    @Override
    public Mono<TechnicalDTO> getbyId(String technicalid) {
        return technicalRepository.findById(technicalid).map(technicalMapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Tecnico no encontrado")));
    }

    @Override
    public Mono<TechnicalDTO> register(TechnicalDTO technicalDTO) {
        return identityClient.isValidUser(technicalDTO.userId()).flatMap(isValid -> {
            if (isValid) {
                Technical technical = technicalMapper.toEntity(technicalDTO);
                technical.setId(UUID.randomUUID().toString());
                technical.setDisponible(true);
                return technicalRepository.save(technical).map(technicalMapper::toDTO);
            }
            return Mono.error(new RuntimeException("Usuario de identidad no encontrado"));
        });

    }

    @Override
    public Mono<TechnicalDTO> update(String technicalId, TechnicalDTO technicalDTO) {
        return technicalRepository.findById(technicalId).flatMap(technical -> {
            technical.setNombre(technicalDTO.name());
            technical.setApellido(technicalDTO.lastName());
            technical.setDni(technicalDTO.dni());
            technical.setTarifa_visita(technicalDTO.visitFee());
            return technicalRepository.save(technical).map(technicalMapper::toDTO);
        }).switchIfEmpty(Mono.error(new RuntimeException("No se encontro el tecnico mediante el id: " + technicalId)));
    }

    @Override
    public Mono<Void> deleteById(String technicalId) {
        return technicalRepository.findById(technicalId)
                .flatMap(value -> {
                    catalogClient.deleteRelatedServices(value.getId());
                    return technicalRepository.delete(value);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontor el tecnico con el id: " + technicalId)));
    }

    @Override
    public Mono<AllTechnicalDTO> getAllAvailable(int pageNumber, int pageSize) {

        Mono<List<TechnicalDTO>> technicals = technicalRepository
                .findAllByDisponibleTrue(PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending()))
                .map(technicalMapper::toDTO)
                .collectList();

        Mono<Long> count = technicalRepository.count();

        return Mono.zip(technicals, count).map(tuple -> {
            List<TechnicalDTO> list = tuple.getT1();
            long totalElements = tuple.getT2();
            int totalPages = (int) Math.ceil((double) totalElements / pageSize);
            boolean last = pageNumber >= totalPages - 1;
            return AllTechnicalDTO.builder()
                    .technicalDTOs(list)
                    .pageNumber(pageNumber)
                    .pageSize(totalElements > 0 ? pageSize : 0)
                    .totalElements(totalElements)
                    .totalPages(totalPages)
                    .last(last)
                    .build();
        });
    }

    @Override
    public Mono<TechnicalDTO> uploadPhotoProfile(String technicalId, Mono<FilePart> filePartMono) {
        return technicalRepository.findById(technicalId)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontro el id de tecnico: " + technicalId)))
                .flatMap(technical -> filePartMono.flatMap(cloudinaryService::uploadImageCloud)
                        .flatMap(secureUrl ->
                        technicalRepository.updatePhotoProfile(technicalId, secureUrl)
                                .then(Mono.fromCallable(() -> {
                                    technical.setUrlFotoPerfil(secureUrl);
                                    return technical;
                                }))))
                .map(technicalMapper::toDTO);
    }

}
