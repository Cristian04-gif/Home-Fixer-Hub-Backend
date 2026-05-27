package com.home_fixer_hub.profile_service.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home_fixer_hub.profile_service.Domain.DTO.TechnicalDTO;
import com.home_fixer_hub.profile_service.Domain.DTO.Response.AllTechnicalDTO;
import com.home_fixer_hub.profile_service.Domain.Service.TechnicalService;
import com.home_fixer_hub.profile_service.Persistense.Util.Pagination;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@Log4j2
@RequestMapping("/api/profile/technicals")
public class TechnicalController {

    @Autowired
    private TechnicalService technicalService;

    @GetMapping("")
    public Mono<ResponseEntity<AllTechnicalDTO>> getAll(
            @RequestParam(defaultValue = Pagination.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(defaultValue = Pagination.PAGE_SIZE, required = false) int pageSize) {
        return technicalService.getAll(pageNumber, pageSize).map(value -> ResponseEntity.ok().body(value));
    }

    @GetMapping("/available")
    public Mono<ResponseEntity<AllTechnicalDTO>> getAllAvailable(
            @RequestParam(defaultValue = Pagination.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(defaultValue = Pagination.PAGE_SIZE, required = false) int pageSize) {
        return technicalService.getAllAvailable(pageNumber, pageSize).map(value -> ResponseEntity.ok(value));
    }

    @GetMapping("/{technicalId}")
    public Mono<ResponseEntity<TechnicalDTO>> getById(@PathVariable String technicalId) {
        return technicalService.getbyId(technicalId).map(value -> ResponseEntity.ok(value)).onErrorResume(e -> {
            log.error("No se encontro el tecnico con el ID, {}", technicalId, e);
            return Mono.just(ResponseEntity.notFound().build());
        });
    }

    @GetMapping("/fixer/user/{userId}")
    public Mono<ResponseEntity<TechnicalDTO>> getbyUserId(@PathVariable String userId){
        return technicalService.getByuserId(userId).map(value -> ResponseEntity.ok(value)).onErrorResume(e ->{
            log.error("no se encontro el tecnico relacionado con el id de usuarios: "+userId);
            return Mono.just(ResponseEntity.notFound().build());
        });
    }

    @PostMapping("/fixer")
    public Mono<ResponseEntity<TechnicalDTO>> register(@RequestBody TechnicalDTO technicalDTO) {
        return technicalService.register(technicalDTO)
                .map(value -> ResponseEntity.status(HttpStatus.CREATED).body(value)).onErrorResume(e -> {
                    log.error("no se pudo registrar el tecnico, {}", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @PostMapping(value = "/fixer/{technicalId}/upload-perfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<TechnicalDTO>> uploadPhotoProfile(@PathVariable String technicalId,
            @RequestPart("file") Mono<FilePart> filePartMono) {
        return technicalService.uploadPhotoProfile(technicalId, filePartMono)
                .map(value -> ResponseEntity.ok(value)).onErrorResume(e -> {
                    log.error("No se pudo guardar la foto del tecnico, {}", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                });
    }

    @PutMapping("/fixer/{technicalId}")
    public Mono<ResponseEntity<TechnicalDTO>> update(@PathVariable String technicalId, TechnicalDTO dto) {
        return technicalService.update(technicalId, dto)
                .map(value -> ResponseEntity.status(HttpStatus.ACCEPTED).body(value)).onErrorResume(e -> {
                    log.error("No se puedo actualizar el tecnico ", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                });
    }

    @DeleteMapping("/fixer/{technicalId}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String technicalId) {
        return technicalService.deleteById(technicalId).map(vales -> ResponseEntity.noContent().build());
    }

}
