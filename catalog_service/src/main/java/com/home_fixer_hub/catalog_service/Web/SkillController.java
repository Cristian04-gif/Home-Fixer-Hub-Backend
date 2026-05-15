package com.home_fixer_hub.catalog_service.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home_fixer_hub.catalog_service.Domain.DTO.TechnicalServiceDTO;
import com.home_fixer_hub.catalog_service.Domain.Service.TechnicalServiceService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Log4j2
@RequestMapping("/api/catalog/skills")
public class SkillController {

    @Autowired
    private TechnicalServiceService service;

    @PostMapping("/assing")
    public Mono<ResponseEntity<TechnicalServiceDTO>> assignServiceToTechnician(@RequestBody TechnicalServiceDTO dto) {
        return service.assignSkill(dto.technicalId(), dto.serviceId())
                .map(value -> ResponseEntity.status(HttpStatus.CREATED).body(value)).onErrorResume(e -> {
                    log.error("No se pudo asignar el servicio al tecnico, {}", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                });
    }

}
