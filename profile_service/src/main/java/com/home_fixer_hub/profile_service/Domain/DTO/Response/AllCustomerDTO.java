package com.home_fixer_hub.profile_service.Domain.DTO.Response;

import java.util.List;

import com.home_fixer_hub.profile_service.Domain.DTO.CustomerDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AllCustomerDTO {

    private List<CustomerDTO> customerDTOs;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
