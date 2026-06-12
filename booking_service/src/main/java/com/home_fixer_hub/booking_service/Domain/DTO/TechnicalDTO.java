package com.home_fixer_hub.booking_service.Domain.DTO;

public record TechnicalDTO(
        String id,
        String name,
        String lastName,
        String dni,
        Boolean available,
        String userId,
        String urlPhotoProfile,
        Double averageRating) {

}
