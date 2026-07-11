package com.home_fixer_hub.review_service.Web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home_fixer_hub.review_service.Domain.DTO.ReviewRequest;
import com.home_fixer_hub.review_service.Domain.Service.ReviewService;
import com.home_fixer_hub.review_service.Persistense.Models.Review;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/reviews")
@Log4j2
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/customer")
    public Mono<ResponseEntity<Void>> registerReview(@RequestBody ReviewRequest request) {
        return reviewService.register(request).map(value -> ResponseEntity.noContent().build());
    }

    @GetMapping("/{reviewId}")
    public Mono<ResponseEntity<Review>> getbyId(@PathVariable String reviewId){
        return reviewService.getById(reviewId).map(value -> ResponseEntity.ok(value)).onErrorResume(e ->{
            log.error("No se encontro la valoracion {}", e);
            return Mono.just(ResponseEntity.notFound().build());
        });
    }

    @GetMapping("/technical/{technicalId}")
    public Flux<Review> getByTechnical(@PathVariable String technicalId){
        return reviewService.getByTechnical(technicalId);
    }
    

}
