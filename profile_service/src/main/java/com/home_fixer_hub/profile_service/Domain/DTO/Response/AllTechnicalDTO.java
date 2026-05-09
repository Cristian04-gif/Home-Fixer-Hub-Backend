package com.home_fixer_hub.profile_service.Domain.DTO.Response;

import java.util.List;

import com.home_fixer_hub.profile_service.Domain.DTO.TechnicalDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AllTechnicalDTO {
    private List<TechnicalDTO> technicalDTOs;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
