package com.home_fixer_hub.profile_service.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home_fixer_hub.profile_service.Domain.DTO.CustomerDTO;
import com.home_fixer_hub.profile_service.Domain.DTO.Response.AllCustomerDTO;
import com.home_fixer_hub.profile_service.Domain.Service.CustomerService;
import com.home_fixer_hub.profile_service.Persistense.Util.Pagination;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@Log4j2
@RequestMapping("/api/profile/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("")
    public Mono<ResponseEntity<AllCustomerDTO>> getAll(
            @RequestParam(defaultValue = Pagination.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(defaultValue = Pagination.PAGE_SIZE, required = false) int pageSize) {
        return customerService.getAll(pageNumber, pageSize).map(value -> ResponseEntity.ok().body(value));
    }

    @GetMapping("/{customerId}")
    public Mono<ResponseEntity<CustomerDTO>> getById(@PathVariable String customerId) {
        return customerService.getById(customerId).map(value -> ResponseEntity.ok(value)).onErrorResume(e -> {
            log.error("no se encontro el cliente por el id, {}", customerId, e);
            return Mono.just(ResponseEntity.notFound().build());
        });
    }

    @GetMapping("/customer/userId/{userId}")
    public Mono<ResponseEntity<CustomerDTO>> getByUserId(@PathVariable String userId) {
        return customerService.getByUserId(userId).map(value -> ResponseEntity.ok(value)).onErrorResume(e -> {
            log.error("No se encontro el user id, {}", e);
            return Mono.just(ResponseEntity.notFound().build());
        });
    }

    @PostMapping("/customer")
    public Mono<ResponseEntity<CustomerDTO>> register(@RequestHeader("Authorization") String auth,
            @RequestBody CustomerDTO customerDTO) {
        return customerService.register(customerDTO)
                .map(value -> ResponseEntity.status(HttpStatus.CREATED).body(value)).onErrorResume(e -> {
                    log.error("no se pudo registrar el cliente, {}", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    @PostMapping(value = "/customer/{customerId}/upload-perfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<CustomerDTO>> uploadPhotoProfile(@PathVariable String customerId,
            @RequestPart("file") Mono<FilePart> filePartMono) {
        System.out.println("entro al registro de foto");
        return customerService.uploadPhotoProfile(customerId, filePartMono)
                .map(value -> ResponseEntity.ok(value)).onErrorResume(e -> {
                    log.error("No se pudo guardar la foto del cliente, {}", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
                });
    }

}
