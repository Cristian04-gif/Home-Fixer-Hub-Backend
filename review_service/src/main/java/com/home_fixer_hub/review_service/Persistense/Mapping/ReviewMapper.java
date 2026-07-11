package com.home_fixer_hub.review_service.Persistense.Mapping;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.home_fixer_hub.review_service.Domain.DTO.ReviewRequest;
import com.home_fixer_hub.review_service.Persistense.Models.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mappings({
            @Mapping(source = "comentario", target = "comment"),
            @Mapping(source = "puntuacion", target = "punctuation"),
            @Mapping(source = "idConsulta", target = "bookingId"),
            @Mapping(source = "idTecnico", target = "technicalId")
    })

    ReviewRequest toDTO(Review review);

    @InheritInverseConfiguration
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaPublicacion", ignore = true)
    Review toEntity(ReviewRequest reviewRequest);
}
