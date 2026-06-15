package com.home_fixer_hub.identity_service.Web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home_fixer_hub.identity_service.Domain.DTO.UserDTO;
import com.home_fixer_hub.identity_service.Domain.DTO.Request.AuthRequest;
import com.home_fixer_hub.identity_service.Domain.DTO.Response.AuthResponse;
import com.home_fixer_hub.identity_service.Domain.Service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@Log4j2
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest authRequest) {
        return userService.login(authRequest)
                .map(login -> ResponseEntity.ok(new AuthResponse(login.getUserId(), login.getToken())))
                .onErrorResume(e -> {
                    log.error("No se pudo iniciar sesion con el usuario {}", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                });
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<AuthResponse>> register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO).map(value -> ResponseEntity.status(201).body(value))
                .onErrorResume(e -> {
                    log.error("No se pudo registrar el usuario {}", e);
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    @PostMapping("/refresh")
    public Mono<ResponseEntity<AuthResponse>> refreshToken(@RequestHeader("Authorization") String token) {
        return userService.refreshToken(token).map(value -> ResponseEntity.ok(value)).onErrorResume(e -> {
            log.error("No se identifico el token {}", e);
            return Mono.just(ResponseEntity.notFound().build());
        });
    }

    @GetMapping("/validate/{userId}")
    public Mono<ResponseEntity<Void>> validateUser(@PathVariable String userId) {
        return userService.validateUser(userId)
                .map(user -> ResponseEntity.ok().<Void>build())
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
}
