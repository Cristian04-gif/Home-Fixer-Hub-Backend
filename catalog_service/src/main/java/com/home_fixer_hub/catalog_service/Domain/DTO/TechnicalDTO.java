package com.home_fixer_hub.catalog_service.Domain.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class TechnicalDTO {

        private String id;
        private String name;
        private String lastName;
        private String dni;
        private Boolean available;
        private String userId;
        private String urlPhotoProfile;
        private Double averageRating;
        private String description;
        private BigDecimal priceBase;

}
