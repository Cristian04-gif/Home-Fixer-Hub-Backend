package com.home_fixer_hub.identity_service.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home_fixer_hub.identity_service.Domain.DTO.UserDTO;
import com.home_fixer_hub.identity_service.Domain.DTO.Request.AuthRequest;
import com.home_fixer_hub.identity_service.Domain.DTO.Response.AuthResponse;
import com.home_fixer_hub.identity_service.Domain.DTO.Response.UserResponse;
import com.home_fixer_hub.identity_service.Domain.Service.UserService;
import com.home_fixer_hub.identity_service.Persistense.Utils.Pagination;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Log4j2
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin/all-users")
    public Mono<ResponseEntity<UserResponse>> getAll(

            @RequestParam(defaultValue = Pagination.PAGE_NUMBER, required = false) int page,

            @RequestParam(defaultValue = Pagination.PAGE_SIZE, required = false) int size) {
        return userService.getAll(page, size).map(value -> ResponseEntity.ok().body(value));
    }

    /*
     * @GetMapping("/{userId}")
     * public Mono<ResponseEntity<UserDTO>> getById(@PathVariable String userId) {
     * return userService.getById(userId).map(value ->
     * ResponseEntity.ok(value)).onErrorResume(e -> {
     * log.error("NO se encontro el usuario con ID: {}", userId, e);
     * return Mono.just(ResponseEntity.notFound().build());
     * });
     * 
     * }
     */

    @PostMapping("/register")
    public Mono<ResponseEntity<AuthResponse>> register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO).map(value -> ResponseEntity.status(201).body(value))
                .onErrorResume(e -> {
                    log.error("No se pudo registrar el usuario {}", e);
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest authRequest) {
        return userService.login(authRequest)
                .map(login -> ResponseEntity.ok(new AuthResponse(login.getUserId(), login.getToken())))
                .onErrorResume(e -> {
                    log.error("No se pudo iniciar sesion con el usuario {}", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                });
    }

    @GetMapping("/validate/{userId}")
    public Mono<ResponseEntity<Void>> validateUser(@PathVariable String userId) {
        return userService.validateUser(userId)
                .map(user -> ResponseEntity.ok().<Void>build())
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

}
