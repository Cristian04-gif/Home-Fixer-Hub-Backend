package com.home_fixer_hub.identity_service.Domain.DTO;

import java.time.LocalDate;

public record UserDTO(
        String id,
        String email,
        String password,
        LocalDate registrationDate,
        String role) {

}
