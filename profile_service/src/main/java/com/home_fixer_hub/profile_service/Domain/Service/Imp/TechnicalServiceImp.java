package com.home_fixer_hub.profile_service.Domain.Service.Imp;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import com.home_fixer_hub.profile_service.Domain.Client.CatalogClient;
import com.home_fixer_hub.profile_service.Domain.Client.IdentityClient;
import com.home_fixer_hub.profile_service.Domain.DTO.TechnicalDTO;
import com.home_fixer_hub.profile_service.Domain.Service.CloudinaryService;
import com.home_fixer_hub.profile_service.Domain.Service.TechnicalService;
import com.home_fixer_hub.profile_service.Persistense.Mapping.TechnicalMapper;
import com.home_fixer_hub.profile_service.Persistense.Model.Technical;
import com.home_fixer_hub.profile_service.Persistense.Repository.TechnicalRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
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
    public Mono<TechnicalDTO> getbyId(String technicalid) {
        return technicalRepository.findById(technicalid).map(technicalMapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("Tecnico no encontrado")));
    }

    @Override
    public Mono<TechnicalDTO> getByuserId(String userId) {
        return technicalRepository.findByIdUsuario(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("NO se encontro el tecnico ocon el userID " + userId)))
                .map(technicalMapper::toDTO);
    }

    @Override
    public Mono<TechnicalDTO> register(TechnicalDTO technicalDTO) {
        return identityClient.isValidUser(technicalDTO.userId()).flatMap(isValid -> {
            if (isValid) {
                Technical technical = technicalMapper.toEntity(technicalDTO);
                technical.setDisponible(true);
                technical.setValoracionPromedio(0.00);
                return technicalRepository.save(technical).map(technicalMapper::toDTO);
            }
            return Mono.error(new RuntimeException("Usuario de identidad no encontrado"));
        });

    }

    @Override
    public Mono<TechnicalDTO> uploadPhotoProfile(String technicalId, Mono<FilePart> filePartMono) {
        return technicalRepository.findById(technicalId)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontro el id de tecnico: " + technicalId)))
                .flatMap(technical -> filePartMono.flatMap(cloudinaryService::uploadImageCloud)
                        .flatMap(secureUrl -> technicalRepository.updatePhotoProfile(technicalId, secureUrl)
                                .then(Mono.fromCallable(() -> {
                                    technical.setUrlFotoPerfil(secureUrl);
                                    return technical;
                                }))))
                .map(technicalMapper::toDTO);
    }

    @Override
    public Mono<TechnicalDTO> update(String technicalId, TechnicalDTO technicalDTO) {
        return technicalRepository.findById(technicalId).flatMap(technical -> {
            technical.setNombre(technicalDTO.name());
            technical.setApellido(technicalDTO.lastName());
            technical.setDni(technicalDTO.dni());
            return technicalRepository.save(technical).map(technicalMapper::toDTO);
        }).switchIfEmpty(Mono.error(new RuntimeException("No se encontro el tecnico mediante el id: " + technicalId)));
    }

    @Override
    public Mono<TechnicalDTO> changeAvailability(String technicalId) {
        return technicalRepository.findById(technicalId)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontro el tecnico con el id: " + technicalId)))
                .flatMap(technical -> {
                    technical.setDisponible(!technical.getDisponible());
                    return technicalRepository.save(technical);
                }).map(technicalMapper::toDTO);
        /*
         * .flatMap(technical -> technicalRepository.updateAvailability(technicalId,
         * !technical.getDisponible())
         * .then(Mono.fromCallable(() -> {
         * technical.setDisponible(!technical.getDisponible());
         * return technical;
         * })))
         */

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

    ///////////////////////////

    @Override
    public Flux<TechnicalDTO>  getAll() {

        return technicalRepository.findAll().map(technicalMapper::toDTO);

    }

    @Override
    public Flux<TechnicalDTO> getAllAvailable() {

        return technicalRepository.findAllByDisponibleTrue().map(technicalMapper::toDTO);
    }
}
