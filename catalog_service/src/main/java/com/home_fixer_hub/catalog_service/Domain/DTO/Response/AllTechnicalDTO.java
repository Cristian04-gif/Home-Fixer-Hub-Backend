package com.home_fixer_hub.catalog_service.Domain.DTO.Response;

import java.util.List;

import com.home_fixer_hub.catalog_service.Domain.DTO.TechnicalDTO;
import com.home_fixer_hub.catalog_service.Domain.DTO.TypeServiceDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AllTechnicalDTO {
    private TypeServiceDTO service;
    private List<TechnicalDTO> technicals;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
