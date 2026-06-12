package com.home_fixer_hub.identity_service.Domain.Service;

import com.home_fixer_hub.identity_service.Domain.DTO.UserDTO;
import com.home_fixer_hub.identity_service.Domain.DTO.Request.AuthRequest;
import com.home_fixer_hub.identity_service.Domain.DTO.Response.AuthResponse;

import reactor.core.publisher.Mono;

public interface UserService {

    public Mono<UserDTO> getById(String userId);
    public Mono<AuthResponse> register(UserDTO userDTO);
    public Mono<AuthResponse> login(AuthRequest authRequest);
    public Mono<AuthResponse> refreshToken(String token);
    public Mono<Boolean> validateUser(String userId);
    //public Mono<Void> delete(String userId);
}
