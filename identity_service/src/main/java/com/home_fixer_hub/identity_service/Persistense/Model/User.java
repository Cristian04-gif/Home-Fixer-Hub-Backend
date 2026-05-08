package com.home_fixer_hub.identity_service.Persistense.Model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

//import com.home_fixer_hub.identity_service.Persistense.Model.Enum.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("usuarios")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Persistable<String>{

    @Id
    private String id;
    private String email;
    @Column(value = "contraseña")
    private String contrasena;
    private LocalDate fechaRegistro;
    private String rol;

    @Transient
    @Builder.Default
    private boolean isNew = true;

}
