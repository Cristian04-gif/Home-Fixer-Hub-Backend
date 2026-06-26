package com.home_fixer_hub.catalog_service.Domain.DTO.Response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TechnicalServiceResponse {
    private String id;
    private String name;
    private String serviceId;
    private BigDecimal basePrice;
}
