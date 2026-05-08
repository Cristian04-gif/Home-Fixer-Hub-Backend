package com.home_fixer_hub.identity_service.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home_fixer_hub.identity_service.Domain.DTO.UserDTO;
import com.home_fixer_hub.identity_service.Domain.DTO.Response.UserResponse;
import com.home_fixer_hub.identity_service.Domain.Service.UserService;
import com.home_fixer_hub.identity_service.Persistense.Utils.Pagination;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Log4j2
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public Mono<ResponseEntity<UserResponse>> getAll(
            @RequestParam(defaultValue = Pagination.PAGE_NUMBER, required = false) int page,
            @RequestParam(defaultValue = Pagination.PAGE_SIZE, required = false) int size) {
        return userService.getAll(page, size).map(value -> ResponseEntity.ok().body(value));
    }

    @GetMapping("/{userId}")
    public Mono<ResponseEntity<UserDTO>> getById(@PathVariable String userId) {
        return userService.getById(userId).map(value -> ResponseEntity.ok(value)).onErrorResume(e -> {
            log.error("NO se encontro el usuario con ID: {}", userId, e);
            return Mono.just(ResponseEntity.notFound().build());
        });

    }

    @PostMapping("")
    public Mono<ResponseEntity<UserDTO>> register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO).map(value -> ResponseEntity.status(201).body(value))
                .onErrorResume(e -> {
                    log.error("No se pudo registrar el usuario {}", e);
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

}
