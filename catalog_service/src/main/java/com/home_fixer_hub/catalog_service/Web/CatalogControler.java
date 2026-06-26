package com.home_fixer_hub.catalog_service.Web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home_fixer_hub.catalog_service.Domain.DTO.TechnicalDTO;
import com.home_fixer_hub.catalog_service.Domain.DTO.TechnicalServiceDTO;
import com.home_fixer_hub.catalog_service.Domain.Service.TechnicalServiceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@Log4j2
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
public class CatalogControler {

    private final TechnicalServiceService service;

    @GetMapping("/public/service/{serviceId}/tecnicals")
    public Flux<TechnicalDTO> getTechniciansPerService(@PathVariable String serviceId) {
        return service.getTechnicalsByService(serviceId);
    }

    @GetMapping("/public/technical-service/technical/{technicalId}/service/{serviceId}")
    public Flux<TechnicalServiceDTO> getByTeachnicalAndService(@PathVariable String technicalId,
            @PathVariable String serviceId) {
        return service.getByTecnicalIdAndServiceId(technicalId, serviceId);
    }

}
