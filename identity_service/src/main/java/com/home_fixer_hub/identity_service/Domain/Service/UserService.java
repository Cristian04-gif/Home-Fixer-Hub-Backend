package com.home_fixer_hub.identity_service.Domain.Service;

import com.home_fixer_hub.identity_service.Domain.DTO.UserDTO;
import com.home_fixer_hub.identity_service.Domain.DTO.Request.AuthRequest;
import com.home_fixer_hub.identity_service.Domain.DTO.Response.UserResponse;

import reactor.core.publisher.Mono;

public interface UserService {

    public Mono<UserResponse> getAll(int page, int size);
    //public Mono<UserDTO> getById(String userId);
    public Mono<UserDTO> register(UserDTO userDTO);
    public Mono<String> login(AuthRequest authRequest);
    //public Mono<Void> delete(String userId);
}
