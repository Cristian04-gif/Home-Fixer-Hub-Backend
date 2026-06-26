package com.home_fixer_hub.catalog_service.Persitense.Model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.home_fixer_hub.catalog_service.Persitense.Utils.HasUuid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("tecnico_servicio")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TechnicalService implements HasUuid {
    @Id
    private String id;
    private String nombre;
    private String idTecnico;
    private String idServicio;
    private String descripcion;
    private BigDecimal precioBase;
}
