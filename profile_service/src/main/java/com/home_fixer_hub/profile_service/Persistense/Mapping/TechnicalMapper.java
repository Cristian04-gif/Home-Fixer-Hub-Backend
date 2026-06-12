package com.home_fixer_hub.profile_service.Persistense.Mapping;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.home_fixer_hub.profile_service.Domain.DTO.TechnicalDTO;
import com.home_fixer_hub.profile_service.Persistense.Model.Technical;

@Mapper(componentModel = "spring")
public interface TechnicalMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "nombre", target = "name"),
            @Mapping(source = "apellido", target = "lastName"),
            @Mapping(source = "dni", target = "dni"),
            @Mapping(source = "disponible", target = "available"),
            @Mapping(source = "idUsuario", target = "userId"),
            @Mapping(source = "urlFotoPerfil", target = "urlPhotoProfile")
    })

    TechnicalDTO toDTO(Technical technical);

    @InheritInverseConfiguration
    Technical toEntity(TechnicalDTO technicalDTO);
}
