package com.home_fixer_hub.catalog_service.Domain.DTO;

import java.math.BigDecimal;

public record TechnicalServiceDTO(
    String id,
    String name,
    String technicalId,
    String serviceId,
    String description,
    BigDecimal basePrice
) {

}
