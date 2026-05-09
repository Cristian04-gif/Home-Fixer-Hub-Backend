package com.home_fixer_hub.profile_service.Persistense.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("clientes")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Customer implements Persistable<String> {
    @Id
    private String id;
    private String nombre;
    private String apellido;
    private String dni;
    private String id_usuario; 

    @Transient
    @Builder.Default
    private boolean isNew = true;
}