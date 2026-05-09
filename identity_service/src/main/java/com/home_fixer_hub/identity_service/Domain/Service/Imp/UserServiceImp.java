package com.home_fixer_hub.identity_service.Domain.Service.Imp;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.home_fixer_hub.identity_service.Domain.DTO.UserDTO;
import com.home_fixer_hub.identity_service.Domain.DTO.Request.AuthRequest;
import com.home_fixer_hub.identity_service.Domain.DTO.Response.UserResponse;
import com.home_fixer_hub.identity_service.Domain.Service.JwtService;
import com.home_fixer_hub.identity_service.Domain.Service.UserService;
import com.home_fixer_hub.identity_service.Persistense.Mapping.UserMapper;
import com.home_fixer_hub.identity_service.Persistense.Model.User;
import com.home_fixer_hub.identity_service.Persistense.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserResponse> getAll(int page, int size) {

        Mono<List<UserDTO>> userFlux = userRepository.findAllBy(PageRequest.of(page,
                size, Sort.by("id").ascending()))
                .map(userMapper::toDTO).collectList();

        Mono<Long> count = userRepository.count();

        return Mono.zip(userFlux, count).map(tuple -> {
            List<UserDTO> users = tuple.getT1();
            long totalElements = tuple.getT2();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            boolean last = page >= totalPages - 1;
            return UserResponse.builder()
                    .users(users)
                    .pageNumber(page)
                    .pageSize(totalElements > 0 ? size : 0)
                    .totalElements(totalElements)
                    .totalPages(totalPages)
                    .last(last)
                    .build();
        });

    }

    /*
     * @Override
     * public Mono<UserDTO> getById(String userId) {
     * return userRepository.findById(userId).map(userMapper::toDTO)
     * .switchIfEmpty(Mono.error(new
     * RuntimeException("No se encontro el usuarios")));
     * }
     */

    @Override
    public Mono<UserDTO> register(UserDTO userDTO) {
        return userRepository.existsByEmail(userDTO.email()).flatMap(exists -> {
            if (exists) {
                return Mono.error(new RuntimeException("El email ya esta registrado"));
            } else {
                User user = userMapper.toEntity(userDTO);
                user.setId(UUID.randomUUID().toString());
                user.setFechaRegistro(LocalDate.now());
                user.setContrasena(passwordEncoder.encode(userDTO.password()));
                return userRepository.save(user).map(userMapper::toDTO);
            }
        }).onErrorResume(e -> Mono.error(new RuntimeException("Error al registrar el usuario: " + e.getMessage())));
    }

    @Override
    public Mono<String> login(AuthRequest request) {
        return userRepository.findByEmail(request.email())
                .filter(user -> passwordEncoder.matches(request.password(), user.getContrasena()))
                .map(user -> jwtService.generateToken(user.getEmail(), user.getRol()))
                .switchIfEmpty(Mono.error(new RuntimeException("Credenciales inválidas")));
    }

    /*
     * @Override
     * public Mono<Void> delete(String userId) {
     * return userRepository.findById(userId).flatMap(userRepository::delete)
     * .switchIfEmpty(Mono.error(new
     * RuntimeException("No se encontro el usuario a eliminar")));
     * }
     */

}
