package com.home_fixer_hub.catalog_service.Persitense.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.home_fixer_hub.catalog_service.Persitense.Utils.HasUuid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("servicios")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TypeService implements HasUuid {
    @Id
    private String id;
    private String nombre;
    private String icono;
    private String descripcion;

}
