package com.home_fixer_hub.review_service.Persistense.Models;

import java.time.LocalDate;

import org.springframework.data.relational.core.mapping.Table;

import com.home_fixer_hub.review_service.Persistense.Utils.HasUuid;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table("valoraciones")
@Data
@NoArgsConstructor
public class Review implements HasUuid {
    private String id;
    private String comentario;
    private LocalDate fechaPublicacion;
    private Integer puntuacion;
    private String idConsulta;
    private String idTecnico;
}
