package com.home_fixer_hub.catalog_service.Domain.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class TechnicalSkills {
    private String id;
    private String serviceId;
    private String technicalId;
    private String nameService;
    private String iconService;
    private String description;
    private BigDecimal basePrice;
    private Boolean available;
}
