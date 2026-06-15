package com.home_fixer_hub.catalog_service.Web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home_fixer_hub.catalog_service.Domain.DTO.TechnicalServiceDTO;
import com.home_fixer_hub.catalog_service.Domain.DTO.Response.AllTechnicalDTO;
import com.home_fixer_hub.catalog_service.Domain.Service.TechnicalServiceService;
import com.home_fixer_hub.catalog_service.Persitense.Utils.Pagination;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@Log4j2
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
public class CatalogControler {

    private final TechnicalServiceService service;

    @GetMapping("/public/service/{serviceId}/tecnicals")
    public Mono<ResponseEntity<AllTechnicalDTO>> getTechniciansPerService(@PathVariable String serviceId,
            @RequestParam(defaultValue = Pagination.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(defaultValue = Pagination.PAGE_SIZE, required = false) int pageSize) {
        return service.getTechnicalsByService(serviceId, pageNumber, pageSize).map(value -> ResponseEntity.ok(value))
                .onErrorResume(e -> {
                    log.error("ocurrio un error al buscar los tecnicos del servicio {}", e);
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    @GetMapping("/public/technical-service/technical/{technicalId}/service/{serviceId}")
    public Mono<ResponseEntity<TechnicalServiceDTO>> getByTeachnicalAndService(@PathVariable String technicalId,
            @PathVariable String serviceId) {
        return service.getByTecnicalIdAndServiceId(technicalId, serviceId).map(value -> ResponseEntity.ok(value))
                .onErrorResume(e -> {
                    log.error("NO se pudo optener la informacion de tecnico servicio, {}", e);
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

}
