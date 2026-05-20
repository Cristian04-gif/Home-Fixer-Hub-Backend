package com.home_fixer_hub.catalog_service.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home_fixer_hub.catalog_service.Domain.DTO.TypeServiceDTO;
import com.home_fixer_hub.catalog_service.Domain.Service.TypeServiceService;

import jakarta.ws.rs.HeaderParam;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@Log4j2
@RequestMapping("/api/catalog")
public class TypeServiceController {

    @Autowired
    private TypeServiceService typeServiceService;

    @GetMapping("/public/services")
    public ResponseEntity<Flux<TypeServiceDTO>> getAll() {
        return new ResponseEntity<>(typeServiceService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/public/service/{typeServiceId}")
    public Mono<ResponseEntity<TypeServiceDTO>> getById(@PathVariable String typeServiceId) {
        return typeServiceService.getById(typeServiceId).map(value -> ResponseEntity.ok().body(value))
                .onErrorResume(e -> {
                    log.error("Error occurred while fetching type service by ID: {}", typeServiceId, e);
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                });
    }

    @PostMapping("/admin/service")
    public Mono<ResponseEntity<TypeServiceDTO>> register(@RequestHeader("Authorization") String auth,@RequestBody TypeServiceDTO typeServiceDTO) {
        System.out.println(auth);
        return typeServiceService.register(typeServiceDTO)
                .map(value -> ResponseEntity.status(HttpStatus.CREATED).body(value))
                .onErrorResume(e -> {
                    log.error("Error occurred while regisering type service: {}", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                });
    }

    @PutMapping("/admin/service/{serviceId}")
    public Mono<ResponseEntity<TypeServiceDTO>> update(@PathVariable String serviceId,
            @RequestBody TypeServiceDTO dto) {
        return typeServiceService.update(serviceId, dto)
                .map(value -> ResponseEntity.status(HttpStatus.ACCEPTED).body(value))
                .onErrorResume(e -> {
                    log.error("Se produjo un error en la actualizacion del serviico", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                });
    }

    @DeleteMapping("/admin/service/{serviceId}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String serviceId) {
        return typeServiceService.deleteById(serviceId).map(value -> ResponseEntity.noContent().build());

    }
}