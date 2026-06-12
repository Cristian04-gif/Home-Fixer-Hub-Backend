package com.home_fixer_hub.catalog_service.Persitense.Mapping;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.home_fixer_hub.catalog_service.Domain.DTO.ImagesDTO;
import com.home_fixer_hub.catalog_service.Persitense.Model.Images;

@Mapper(componentModel = "spring")
public interface ImagesMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "url", target = "url"),
            @Mapping(source = "fechaRegistro", target = "registrationDate"),
            @Mapping(source = "idTecnicoServicio", target = "technicalServiceId")
    })

    ImagesDTO toDTO(Images images);

    @InheritInverseConfiguration
    Images toEntity(ImagesDTO imagesDTO);
}
