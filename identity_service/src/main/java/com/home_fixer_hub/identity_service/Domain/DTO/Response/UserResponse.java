package com.home_fixer_hub.identity_service.Domain.DTO.Response;

import java.util.List;

import com.home_fixer_hub.identity_service.Domain.DTO.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserResponse {

    private List<UserDTO> users;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
