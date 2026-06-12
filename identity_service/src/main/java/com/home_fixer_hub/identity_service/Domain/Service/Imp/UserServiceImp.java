package com.home_fixer_hub.identity_service.Domain.Service.Imp;

import java.time.LocalDate;
import java.util.Base64;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.home_fixer_hub.identity_service.Domain.DTO.UserDTO;
import com.home_fixer_hub.identity_service.Domain.DTO.Request.AuthRequest;
import com.home_fixer_hub.identity_service.Domain.DTO.Response.AuthResponse;
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
    public Mono<UserDTO> getById(String userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontor el usuario con el id: " + userId)))
                .map(userMapper::toDTO);
    }

    @Override
    public Mono<AuthResponse> register(UserDTO userDTO) {
        return userRepository.existsByEmail(userDTO.email()).flatMap(exists -> {
            if (exists) {
                return Mono.error(new RuntimeException("El email ya esta registrado"));
            } else {
                User user = userMapper.toEntity(userDTO);
                user.setFechaRegistro(LocalDate.now());
                user.setContrasena(passwordEncoder.encode(userDTO.password()));
                if (user.getEmail().endsWith("@tuapp.com")) {
                    user.setRol("ADMIN");
                }
                return userRepository.save(user).map(userMapper::toDTO);
            }
        }).map(user -> {
            String token = jwtService.generateToken(user.email(), user.role(), user.id());
            return new AuthResponse(user.id(), token);
        }).onErrorResume(e -> Mono.error(new RuntimeException("Error al registrar el usuario: " + e.getMessage())));
    }

    @Override
    public Mono<AuthResponse> login(AuthRequest request) {
        return userRepository.findByEmail(request.email())
                .filter(user -> passwordEncoder.matches(request.password(), user.getContrasena()))
                .map(user -> {
                    String userId = user.getId();
                    String token = jwtService.generateToken(user.getEmail(), user.getRol(), user.getId());
                    return new AuthResponse(userId, token);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Credenciales inválidas")));
    }

    @Override
    public Mono<Boolean> validateUser(String userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public Mono<AuthResponse> refreshToken(String token) {
        return Mono.fromCallable(() -> {
            String[] chunks = token.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chunks[1]));
            return payload;
        }).flatMap(value -> {
            String userId = value.split(",")[1].split(":")[1].replaceAll("\"", "");
            return userRepository.findById(userId)
                    .switchIfEmpty(Mono.error(new RuntimeException("No se encontró el usuario con el id: " + userId)))
                    .map(user -> {
                        String newToken = jwtService.generateToken(user.getEmail(), user.getRol(), user.getId());
                        return new AuthResponse(user.getId(), newToken);
                    });
        });
    }

}
