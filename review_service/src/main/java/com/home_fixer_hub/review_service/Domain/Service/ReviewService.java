package com.home_fixer_hub.review_service.Domain.Service;

import com.home_fixer_hub.review_service.Domain.DTO.ReviewRequest;
import com.home_fixer_hub.review_service.Persistense.Models.Review;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewService {
    public Mono<Void> register(ReviewRequest reviewRequest);

    public Mono<Review> getById(String reviewId);

    public Flux<Review> getByTechnical(String technicalId);
}
