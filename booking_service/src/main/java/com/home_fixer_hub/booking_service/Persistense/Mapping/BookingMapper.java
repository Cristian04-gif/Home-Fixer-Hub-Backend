package com.home_fixer_hub.booking_service.Persistense.Mapping;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.home_fixer_hub.booking_service.Domain.DTO.BookingDTO;
import com.home_fixer_hub.booking_service.Persistense.Model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "tipoServicio", target = "serviceType"),
            @Mapping(source = "descripcion", target = "description"),
            @Mapping(source = "fechaConsulta", target = "inquiryDate"),
            @Mapping(source = "latitud", target = "latitude"),
            @Mapping(source = "longitud", target = "longitude"),
            @Mapping(source = "direccionDetallada", target = "detailedAddress"),
            @Mapping(source = "montoTotal", target = "totalAmount"),
            @Mapping(source = "estadoConsulta", target = "inquiryStatus"),
            @Mapping(source = "idCliente", target = "customerId"),
            @Mapping(source = "idTecnico", target = "technicalId"),
    })

    BookingDTO toDTO(Booking booking);

    @InheritInverseConfiguration
    Booking toEntity(BookingDTO bookingDTO);
}
