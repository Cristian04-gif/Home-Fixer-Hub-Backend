package com.home_fixer_hub.catalog_service.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.home_fixer_hub.catalog_service.Domain.DTO.ImagesDTO;
import com.home_fixer_hub.catalog_service.Domain.Service.ImagesService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@Log4j2
@RequestMapping("/api/catalog/images")
public class ImagesController {

    @Autowired
    private ImagesService imagesService;

    @GetMapping("/public/technical-service/{technicalServiceId}")
    public Flux<ImagesDTO> getImagesByTechnicalServiceId(@PathVariable String technicalServiceId) {
        return imagesService.getImagesByTechnicalServiceId(technicalServiceId);
    }

    @PostMapping(value = "/fixer/technical-service/{technicalServiceId}/ipload-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Flux<ImagesDTO> assignImagesToTechnicianAndServiceRelationships(@PathVariable String technicalServiceId,
            @RequestPart("files") Flux<FilePart> filePartFlux) {
        return imagesService.assignImagesToTechnicianAndServiceRelationships(technicalServiceId, filePartFlux);
    }

}
