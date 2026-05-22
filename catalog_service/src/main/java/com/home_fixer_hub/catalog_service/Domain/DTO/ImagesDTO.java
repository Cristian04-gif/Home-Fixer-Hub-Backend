package com.home_fixer_hub.catalog_service.Domain.DTO;

import java.time.LocalDate;

public record ImagesDTO(
        String id,
        String url,
        LocalDate registrationDate,
        String technicalServiceId) {

}
