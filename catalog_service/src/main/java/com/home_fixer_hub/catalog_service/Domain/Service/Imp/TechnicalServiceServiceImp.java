package com.home_fixer_hub.catalog_service.Domain.Service.Imp;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.home_fixer_hub.catalog_service.Domain.Client.ProfileClient;
import com.home_fixer_hub.catalog_service.Domain.DTO.TechnicalDTO;
import com.home_fixer_hub.catalog_service.Domain.DTO.TechnicalServiceDTO;
import com.home_fixer_hub.catalog_service.Domain.DTO.TypeServiceDTO;
import com.home_fixer_hub.catalog_service.Domain.DTO.Response.AllTechnicalDTO;
import com.home_fixer_hub.catalog_service.Domain.Service.TechnicalServiceService;
import com.home_fixer_hub.catalog_service.Persitense.Mapping.TechnicalServiceMapper;
import com.home_fixer_hub.catalog_service.Persitense.Mapping.TypeServiceMapper;
import com.home_fixer_hub.catalog_service.Persitense.Model.TechnicalService;
import com.home_fixer_hub.catalog_service.Persitense.Repository.ImagesRepository;
import com.home_fixer_hub.catalog_service.Persitense.Repository.TechnicalServiceRepository;
import com.home_fixer_hub.catalog_service.Persitense.Repository.TypeServiceRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TechnicalServiceServiceImp implements TechnicalServiceService {

        private final TechnicalServiceRepository repository;
        private final TechnicalServiceMapper mapper;
        private final ProfileClient profileClient;

        private final TypeServiceRepository serviceRepository;
        private final TypeServiceMapper serviceMapper;

        private final ImagesRepository imagesRepository;

        @Override
        public Mono<TechnicalServiceDTO> assignSkill(TechnicalServiceDTO technicalServiceDTO) {
                return profileClient.getTechnicalById(technicalServiceDTO.technicalId()).flatMap(profile -> {
                        TechnicalService service = TechnicalService.builder()
                                        .id(UUID.randomUUID().toString())
                                        .idTecnico(technicalServiceDTO.technicalId())
                                        .idServicio(technicalServiceDTO.serviceId())
                                        .descripcion(technicalServiceDTO.description())
                                        .build();
                        return repository.save(service);
                }).map(mapper::toDTO)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("NO se pudo enlazar el servicio con eltecnico")));
        }

        @Override
        public Mono<AllTechnicalDTO> getTechnicalsByService(String serviceId, int pageNumber, int pageSize) {

                Mono<TypeServiceDTO> service = serviceRepository.findById(serviceId).map(serviceMapper::toDTO)
                                .switchIfEmpty(Mono.error(new RuntimeException("No se encontro el servicio")));

                Mono<List<TechnicalDTO>> technicals = repository
                                .findAllByIdServicio(serviceId, PageRequest.of(pageNumber, pageSize))
                                .flatMap(value -> profileClient.getTechnicalById(value.getIdTecnico())).collectList();

                Mono<Long> count = repository.countByIdServicio(serviceId);

                return Mono.zip(service, technicals, count).map(tuple -> {
                        TypeServiceDTO typeService = tuple.getT1();
                        List<TechnicalDTO> list = tuple.getT2();
                        long totalElements = tuple.getT3();
                        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
                        boolean last = pageNumber >= totalPages - 1;
                        return AllTechnicalDTO.builder()
                                        .service(typeService)
                                        .technicals(list)
                                        .pageNumber(pageNumber)
                                        .pageSize(totalElements > 0 ? pageSize : 0)
                                        .totalElements(totalElements)
                                        .totalPages(totalPages)
                                        .last(last)
                                        .build();
                });
        }

        @Override
        public Mono<Void> deleteByTechnical(String technicalId) {
                return profileClient.getTechnicalById(technicalId)
                                .switchIfEmpty(Mono.error(new RuntimeException(
                                                "No se encontro el tecnico con id: " + technicalId)))
                                .flatMapMany(technical -> repository.findAllByIdTecnico(technicalId))
                                .flatMap(value -> imagesRepository.deleteAllByIdTecnicoServicio(value.getId())
                                                .then(repository.delete(value)))
                                .then(); 
        }

        @Override
        public Mono<TechnicalServiceDTO> getByTecnicalIdAndServiceId(String technicalId, String serviceId) {
                return repository.findByIdTecnicoAndIdServicio(technicalId, serviceId).map(mapper::toDTO)
                                .switchIfEmpty(Mono.error(new RuntimeException(
                                                "No se encontro la informacion relaicionada con el tecnico: "
                                                                + technicalId + " y el servicio: " + serviceId)));
        }

        @Override
        public Flux<TypeServiceDTO> getSericesFortechnical(String technicalId) {
                return profileClient.getTechnicalById(technicalId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No se encontro al tecnico " + technicalId)))
                                .flatMapMany(technical -> {
                                        return repository.findAllByIdTecnico(technical.id())
                                                        .flatMap(value -> serviceRepository
                                                                        .findById(value.getIdServicio())
                                                                        .map(serviceMapper::toDTO));
                                });
        }

        @Override
        public Mono<Void> removeSkill(String technicalId, String serviceId) {
                return repository.findByIdTecnicoAndIdServicio(technicalId, serviceId)
                                .switchIfEmpty(Mono.error(new RuntimeException(
                                                "No se pudo eliminar la relacion del tecnico con el servicio")))
                                .flatMap(value -> {
                                        imagesRepository.deleteAllByIdTecnicoServicio(value.getId());
                                        return repository.delete(value);
                                });
        }

}
