package com.home_fixer_hub.identity_service.Domain.DTO.Request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserRegister {

    private String email;
    private String password;
    private String role;

    private String name;
    private String lastName;
    private String dni;

    private BigDecimal visitFee;
}
