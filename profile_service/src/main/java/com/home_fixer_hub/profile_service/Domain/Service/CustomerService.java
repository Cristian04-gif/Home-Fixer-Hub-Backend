package com.home_fixer_hub.profile_service.Domain.Service;

import org.springframework.http.codec.multipart.FilePart;

import com.home_fixer_hub.profile_service.Domain.DTO.CustomerDTO;
import com.home_fixer_hub.profile_service.Domain.DTO.Response.AllCustomerDTO;

import reactor.core.publisher.Mono;

public interface CustomerService {

    public Mono<AllCustomerDTO> getAll(int page, int size);
    public Mono<CustomerDTO> getById(String customerId);
    public Mono<CustomerDTO> register(CustomerDTO customerDTO);
    public Mono<CustomerDTO> uploadPhotoProfile(String customerId, Mono<FilePart> filePartMono);
    public Mono<CustomerDTO> getByUserId(String userId);
}
