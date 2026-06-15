package com.home_fixer_hub.identity_service.Web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home_fixer_hub.identity_service.Domain.DTO.UserDTO;
import com.home_fixer_hub.identity_service.Domain.Service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@RestController
@Log4j2
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;

    @GetMapping("/{userId}")
    public Mono<ResponseEntity<UserDTO>> getById(@PathVariable String userId) {
        return userService.getById(userId).map(value -> ResponseEntity.ok(value)).onErrorResume(e -> {
            log.error("NO se encontro el usuario con ID: {}", userId, e);
            return Mono.just(ResponseEntity.notFound().build());
        });
    }

    
}
