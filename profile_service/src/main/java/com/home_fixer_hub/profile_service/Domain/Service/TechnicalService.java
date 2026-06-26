package com.home_fixer_hub.profile_service.Domain.Service;

import org.springframework.http.codec.multipart.FilePart;

import com.home_fixer_hub.profile_service.Domain.DTO.TechnicalDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TechnicalService {

    public Mono<TechnicalDTO> getbyId(String technicalid);

    public Mono<TechnicalDTO> getByuserId(String userId);

    public Mono<TechnicalDTO> register(TechnicalDTO technicalDTO);

    public Mono<TechnicalDTO> uploadPhotoProfile(String technicalId, Mono<FilePart> filePartMono);

    public Mono<TechnicalDTO> update(String technicalId, TechnicalDTO technicalDTO);

    public Mono<TechnicalDTO> changeAvailability(String technicalId);

    public Mono<Void> deleteById(String technicalId);
    
    ///////////////////////////////////
    /// 
    
    public Flux<TechnicalDTO> getAll();

    public Flux<TechnicalDTO> getAllAvailable();

}
