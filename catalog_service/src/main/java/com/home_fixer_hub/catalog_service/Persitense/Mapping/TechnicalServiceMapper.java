package com.home_fixer_hub.catalog_service.Persitense.Mapping;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.home_fixer_hub.catalog_service.Domain.DTO.TechnicalServiceDTO;
import com.home_fixer_hub.catalog_service.Persitense.Model.TechnicalService;

@Mapper(componentModel = "spring")
public interface TechnicalServiceMapper {

    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "idTecnico", target = "technicalId"),
        @Mapping(source = "idServicio", target = "serviceId"),
        @Mapping(source = "descripcion", target = "description")
    })

    TechnicalServiceDTO toDTO(TechnicalService technicalService);

    @InheritInverseConfiguration
    @Mapping(target = "isNew", ignore = true)
    TechnicalService toEntity(TechnicalServiceDTO technicalServiceDTO);

    
}
