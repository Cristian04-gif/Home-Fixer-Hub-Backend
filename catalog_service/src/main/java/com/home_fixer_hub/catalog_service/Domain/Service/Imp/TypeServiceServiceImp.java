package com.home_fixer_hub.catalog_service.Domain.Service.Imp;

import org.springframework.stereotype.Service;

import com.home_fixer_hub.catalog_service.Domain.DTO.TypeServiceDTO;
import com.home_fixer_hub.catalog_service.Domain.Service.TypeServiceService;
import com.home_fixer_hub.catalog_service.Persitense.Mapping.TypeServiceMapper;
import com.home_fixer_hub.catalog_service.Persitense.Repository.TechnicalServiceRepository;
import com.home_fixer_hub.catalog_service.Persitense.Repository.TypeServiceRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TypeServiceServiceImp implements TypeServiceService {

    private final TypeServiceRepository typeServiceRepository;
    private final TechnicalServiceRepository technicalServiceRepository;
    private final TypeServiceMapper typeServiceMapper;

    @Override
    public Flux<TypeServiceDTO> getAll() {
        return typeServiceRepository.findAll().map(typeServiceMapper::toDTO);
    }

    @Override
    public Mono<TypeServiceDTO> getById(String id) {
        return typeServiceRepository.findById(id).map(typeServiceMapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontro el servicio")));
    }

    @Override
    public Mono<TypeServiceDTO> register(TypeServiceDTO typeServiceDTO) {
        return typeServiceRepository.save(typeServiceMapper.toEntity(typeServiceDTO)).map(typeServiceMapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("No se puedo register el tipo de servicio")));
    }

    @Override
    public Mono<TypeServiceDTO> update(String serviceId, TypeServiceDTO serviceDTO) {
        return typeServiceRepository.findById(serviceId).flatMap(service -> {
            service.setNombre(serviceDTO.name());
            return typeServiceRepository.save(service).map(typeServiceMapper::toDTO);
        }).switchIfEmpty(
                Mono.error(new RuntimeException("No es encontro el tipo de servicio con el id: " + serviceId)));
    }

    @Override
    public Mono<Void> deleteById(String serviceId) {
        return typeServiceRepository.findById(serviceId).flatMap(service -> {
            technicalServiceRepository.deleteAllByIdServicio(service.getId());
            return typeServiceRepository.delete(service);
        }).switchIfEmpty(
                Mono.error(new RuntimeException("No es encontro el tipo de servicio con el id: " + serviceId)));
    }

}
