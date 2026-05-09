package com.home_fixer_hub.profile_service.Domain.DTO;

public record TechnicalDTO(
        String id,
        String name,
        String lastName,
        String dni,
        String visitFee,
        Boolean available,
        String userId) {

}
