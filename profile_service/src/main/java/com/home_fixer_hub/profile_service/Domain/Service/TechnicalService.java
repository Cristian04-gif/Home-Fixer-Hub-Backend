package com.home_fixer_hub.profile_service.Domain.Service;

import org.springframework.http.codec.multipart.FilePart;

import com.home_fixer_hub.profile_service.Domain.DTO.TechnicalDTO;
import com.home_fixer_hub.profile_service.Domain.DTO.Response.AllTechnicalDTO;

import reactor.core.publisher.Mono;

public interface TechnicalService {

    public Mono<AllTechnicalDTO> getAll(int page, int size);

    public Mono<AllTechnicalDTO> getAllAvailable(int pageNumber, int pageSize);

    public Mono<TechnicalDTO> getbyId(String technicalid);

    public Mono<TechnicalDTO> register(TechnicalDTO technicalDTO);

    public Mono<TechnicalDTO> uploadPhotoProfile(String technicalId, Mono<FilePart> filePartMono);

    public Mono<TechnicalDTO> update(String technicalId, TechnicalDTO technicalDTO);

    public Mono<Void> deleteById(String technicalId);

}
