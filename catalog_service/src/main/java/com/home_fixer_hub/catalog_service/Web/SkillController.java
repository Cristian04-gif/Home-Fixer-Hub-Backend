package com.home_fixer_hub.catalog_service.Web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home_fixer_hub.catalog_service.Domain.DTO.TechnicalServiceDTO;
import com.home_fixer_hub.catalog_service.Domain.DTO.TypeServiceDTO;
import com.home_fixer_hub.catalog_service.Domain.DTO.Response.TechnicalSkills;
import com.home_fixer_hub.catalog_service.Domain.Service.TechnicalServiceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@Log4j2
@RequestMapping("/api/catalog/skills")
@RequiredArgsConstructor
public class SkillController {

    private final TechnicalServiceService service;

    @PostMapping("/assing/fixer")
    public Mono<ResponseEntity<TechnicalServiceDTO>> assignServiceToTechnician(@RequestBody TechnicalServiceDTO dto) {
        return service.assignSkill(dto)
                .map(value -> ResponseEntity.status(HttpStatus.CREATED).body(value)).onErrorResume(e -> {
                    log.error("No se pudo asignar el servicio al tecnico, {}", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                });
    }

    @DeleteMapping("/remove/fixer/{technicalId}/service/{serviceId}")
    public Mono<ResponseEntity<Void>> removeSkill(@PathVariable String technicalId, @PathVariable String serviceId) {
        return service.removeSkill(technicalId, serviceId).map(value -> ResponseEntity.noContent().build());
    }

    @GetMapping("/fixer/technical/{technicalId}")
    public Flux<TechnicalSkills> getTechnicianServices(@PathVariable String technicalId) {
        return service.getTechnicianServices(technicalId);
    }

    @DeleteMapping("/fixer/remove-related-services/{technicalId}")
    public Mono<ResponseEntity<Void>> deleteRelatedServices(@PathVariable String technicalId) {
        return service.deleteByTechnical(technicalId).map(value -> ResponseEntity.noContent().build());
    }

}
