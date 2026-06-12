package com.home_fixer_hub.booking_service.Persistense.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.home_fixer_hub.booking_service.Persistense.Utils.HasUuid;

import lombok.Data;

@Table("consultas")
@Data
public class Booking implements HasUuid{

    @Id
    private String id;
    private String tipoServicio;
    private String descripcion;
    private LocalDateTime fechaConsulta;
    private Double latitud;
    private Double longitud;
    private String direccionDetallada;
    private BigDecimal montoTotal;
    private String estadoConsulta;
    private String idCliente;
    private String idTecnico;
}
