package com.home_fixer_hub.catalog_service.Persitense.Mapping;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.home_fixer_hub.catalog_service.Domain.DTO.TypeServiceDTO;
import com.home_fixer_hub.catalog_service.Persitense.Model.TypeService;

@Mapper(componentModel = "spring")
public interface TypeServiceMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "nombre", target = "name"),
    })

    TypeServiceDTO toDTO(TypeService typeService);

    @InheritInverseConfiguration
    //@Mapping(target = "isNew", ignore = true)
    TypeService toEntity(TypeServiceDTO typeServiceDTO);

}
