package com.home_fixer_hub.review_service.Domain.DTO;

public record ReviewRequest(
    String comment,
    Integer punctuation,
    String bookingId,
    String technicalId
) {

}
