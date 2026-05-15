package com.home_fixer_hub.identity_service.Persistense.Utils;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.home_fixer_hub.identity_service.Persistense.Model.User;
import com.home_fixer_hub.identity_service.Persistense.Repository.UserRepository;

import reactor.core.publisher.Mono;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        userRepository.findByEmail("admin@tuapp.com").map(user -> {
            if (user != null) {
                User user2 = User.builder()
                        .id(UUID.randomUUID().toString())
                        .email("admin@tuapp.com")
                        .contrasena(passwordEncoder.encode("admin1234"))
                        .rol("ADMIN")
                        .build();
                        System.out.println(">>> SE HA CREADO EL USUARIO ADMIN INICIAL: admin@tuapp.com / admin1234");
                return userRepository.save(user2);
            }
            return Mono.empty();
        });

    }

}
