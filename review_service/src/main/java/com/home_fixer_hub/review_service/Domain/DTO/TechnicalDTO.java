package com.home_fixer_hub.review_service.Domain.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TechnicalDTO {
        private String id;
        private String name;
        private String lastName;
        private String dni;
        private Boolean available;
        private String userId;
        private String urlPhotoProfile;
        private Double averageRating;
        private String pushToken;
}
