package com.home_fixer_hub.catalog_service.Domain.Service.Imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.home_fixer_hub.catalog_service.Domain.Client.ProfileClient;
import com.home_fixer_hub.catalog_service.Domain.DTO.TechnicalDTO;
import com.home_fixer_hub.catalog_service.Domain.DTO.TechnicalServiceDTO;
import com.home_fixer_hub.catalog_service.Domain.DTO.Response.TechnicalServiceResponse;
import com.home_fixer_hub.catalog_service.Domain.DTO.Response.TechnicalSkills;
import com.home_fixer_hub.catalog_service.Domain.Service.TechnicalServiceService;
import com.home_fixer_hub.catalog_service.Persitense.Mapping.TechnicalServiceMapper;
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
        // private final TypeServiceMapper serviceMapper;

        private final ImagesRepository imagesRepository;

        @Override
        public Mono<TechnicalServiceDTO> assignSkill(TechnicalServiceDTO technicalServiceDTO) {
                return profileClient.getTechnicalById(technicalServiceDTO.technicalId()).flatMap(profile -> {
                        TechnicalService service = TechnicalService.builder()
                                        .nombre(technicalServiceDTO.name())
                                        .idTecnico(technicalServiceDTO.technicalId())
                                        .idServicio(technicalServiceDTO.serviceId())
                                        .descripcion(technicalServiceDTO.description())
                                        .precioBase(technicalServiceDTO.basePrice())
                                        .build();
                        return repository.save(service);
                }).map(mapper::toDTO)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("NO se pudo enlazar el servicio con eltecnico")));
        }

        @Override
        public Flux<TechnicalDTO> getTechnicalsByService(String serviceId) {
                return serviceRepository.findById(serviceId)
                                .switchIfEmpty(Mono.error(new RuntimeException("No se encontró ese servicio")))
                                .flatMapMany(service -> repository.findAllByIdServicio(service.getId()))
                                .flatMap(value -> {

                                        Mono<List<String>> imagesMono = imagesRepository
                                                        .findAllByIdTecnicoServicio(value.getId())
                                                        .map(img -> img.getUrl())
                                                        .collectList();

                                        Mono<TechnicalDTO> profileMono = profileClient
                                                        .getTechnicalById(value.getIdTecnico());

                                        Mono<List<TechnicalServiceResponse>> servicesOffered = profileMono
                                                        .flatMapMany(tech -> repository.findByIdTecnicoAndIdServicio(
                                                                        tech.getId(), serviceId))
                                                        .map(so -> {
                                                                TechnicalServiceResponse response = TechnicalServiceResponse
                                                                                .builder()
                                                                                .id(so.getId())
                                                                                .name(so.getNombre())
                                                                                .serviceId(so.getIdServicio())
                                                                                .basePrice(so.getPrecioBase())
                                                                                .build();
                                                                return response;
                                                        }).collectList();

                                        return Mono.zip(imagesMono, profileMono, servicesOffered)
                                                        .map(tuple -> {
                                                                List<String> urlsImagenes = tuple.getT1();
                                                                TechnicalDTO tech = tuple.getT2();
                                                                List<TechnicalServiceResponse> namesService = tuple
                                                                                .getT3();
                                                                return TechnicalDTO.builder()
                                                                                .id(tech.getId())
                                                                                .name(tech.getName())
                                                                                .lastName(tech.getLastName())
                                                                                .dni(tech.getDni())
                                                                                .available(tech.getAvailable())
                                                                                .userId(tech.getUserId())
                                                                                .urlPhotoProfile(tech
                                                                                                .getUrlPhotoProfile())
                                                                                .averageRating(tech.getAverageRating())
                                                                                .description(value.getDescripcion())
                                                                                .price(value.getPrecioBase())
                                                                                .servicesOffered(namesService)
                                                                                .urlImages(urlsImagenes)
                                                                                .build();
                                                        });
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
        public Flux<TechnicalServiceDTO> getByTecnicalIdAndServiceId(String technicalId, String serviceId) {
                return repository.findByIdTecnicoAndIdServicio(technicalId, serviceId).map(mapper::toDTO)
                                .switchIfEmpty(Mono.error(new RuntimeException(
                                                "No se encontro la informacion relaicionada con el tecnico: "
                                                                + technicalId + " y el servicio: " + serviceId)));
        }

        @Override
        public Mono<Void> removeSkill(String technicalServiceId) {
                return repository.findById(technicalServiceId)
                                .switchIfEmpty(Mono.error(new RuntimeException(
                                                "No se pudo eliminar la relacion del tecnico con el servicio")))
                                .flatMap(value -> {
                                        imagesRepository.deleteAllByIdTecnicoServicio(value.getId());
                                        return repository.delete(value);
                                });
        }

        @Override
        public Flux<TechnicalSkills> getTechnicianServices(String technicalId) {

                Mono<TechnicalDTO> technicalMono = profileClient.getTechnicalById(technicalId)
                                .switchIfEmpty(
                                                Mono.error(new RuntimeException(
                                                                "No se encontró al técnico " + technicalId)));

                return technicalMono.flatMapMany(technical -> repository.findAllByIdTecnico(technicalId)
                                .flatMap(technicalService -> serviceRepository
                                                .findById(technicalService.getIdServicio())
                                                .map(typeService -> TechnicalSkills.builder()
                                                                .id(technicalService.getId())
                                                                .serviceId(typeService.getId())
                                                                .technicalId(technicalId)
                                                                .typeService(typeService.getNombre())
                                                                .nameService(technicalService.getNombre())
                                                                .iconService(typeService.getIcono())
                                                                .description(technicalService.getDescripcion())
                                                                .basePrice(technicalService.getPrecioBase())
                                                                .available(technical.getAvailable())
                                                                .price(technicalService.getPrecioBase())
                                                                .build())));
        }

}
