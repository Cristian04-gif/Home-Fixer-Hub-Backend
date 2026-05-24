package com.home_fixer_hub.catalog_service.Domain.DTO;

import java.math.BigDecimal;

public record TechnicalDTO(
        String id,
        String name,
        String lastName,
        String dni,
        BigDecimal visitFee,
        Boolean available,
        String userId,
        String urlPhotoProfile) {

}
