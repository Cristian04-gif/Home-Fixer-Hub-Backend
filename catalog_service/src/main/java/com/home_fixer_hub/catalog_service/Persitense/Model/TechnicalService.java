package com.home_fixer_hub.catalog_service.Persitense.Model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("tecnico_servicio")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TechnicalService implements Persistable<String>{
    @Id
    private String id;
    private String idTecnico;
    private String idServicio;
    private String descripcion;
    private BigDecimal precioBase;

    @Transient
    @Builder.Default
    private boolean isNew = true;

}
