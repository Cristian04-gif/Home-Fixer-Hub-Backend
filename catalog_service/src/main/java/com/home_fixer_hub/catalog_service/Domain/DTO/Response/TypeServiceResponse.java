package com.home_fixer_hub.catalog_service.Domain.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TypeServiceResponse {
    private String id;
    private String name;
    private String description;
    private Long techNum;
}
