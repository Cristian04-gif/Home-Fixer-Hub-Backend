package com.home_fixer_hub.identity_service.Persistense.Mapping;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.home_fixer_hub.identity_service.Domain.DTO.UserDTO;
import com.home_fixer_hub.identity_service.Persistense.Model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "contrasena", target = "password"),
            @Mapping(source = "fechaRegistro", target = "registrationDate"),
            @Mapping(source = "rol", target = "role"),
    })

    UserDTO toDTO(User user);

    @InheritInverseConfiguration
    @Mapping(target = "isNew", ignore = true)
    User toEntity(UserDTO userDTO);
}
