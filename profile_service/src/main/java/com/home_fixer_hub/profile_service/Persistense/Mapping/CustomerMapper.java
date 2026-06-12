package com.home_fixer_hub.profile_service.Persistense.Mapping;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.home_fixer_hub.profile_service.Domain.DTO.CustomerDTO;
import com.home_fixer_hub.profile_service.Persistense.Model.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "nombre", target = "name"),
        @Mapping(source = "apellido", target = "lastName"),
        @Mapping(source = "dni", target = "dni"),
        @Mapping(source = "idUsuario", target = "userId"),
        @Mapping(source = "urlFotoPerfil", target = "urlPhotoProfile"),
        @Mapping(source = "valoracionPromedio", target = "averageRating")
    })

    CustomerDTO toDTO(Customer customer);

    @InheritInverseConfiguration
    Customer toEntity(CustomerDTO customerDTO);
}
