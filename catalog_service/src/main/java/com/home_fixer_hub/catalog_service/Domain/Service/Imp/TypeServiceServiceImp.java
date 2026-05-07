package com.home_fixer_hub.catalog_service.Domain.Service.Imp;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.home_fixer_hub.catalog_service.Domain.DTO.TypeServiceDTO;
import com.home_fixer_hub.catalog_service.Domain.Service.TypeServiceService;
import com.home_fixer_hub.catalog_service.Persitense.Mapping.TypeServiceMapper;
import com.home_fixer_hub.catalog_service.Persitense.Model.TypeService;
import com.home_fixer_hub.catalog_service.Persitense.Repository.TypeServiceRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TypeServiceServiceImp implements TypeServiceService {

    private final TypeServiceRepository typeServiceRepository;
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
        return Mono.fromCallable((() -> {
            TypeService typeService = typeServiceMapper.toEntity(typeServiceDTO);
            typeService.setId(UUID.randomUUID().toString());
            return typeService;
        })).flatMap(typeServiceRepository::save).map(typeServiceMapper::toDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("No se puedo register el tipo de servicio")));
    }

}
