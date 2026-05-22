package com.home_fixer_hub.catalog_service.Persitense.Model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("imagenes")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Images implements Persistable<String> {

    @Id
    private String id;
    private String url;
    private LocalDate fechaRegistro;
    private String idTecnicoServicio;

    @Transient
    @Builder.Default
    private boolean isNew = true;
}
