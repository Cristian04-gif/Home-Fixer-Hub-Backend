package com.home_fixer_hub.booking_service.Domain.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingDTO(
        String id,
        String serviceType,
        String title,
        String description,
        LocalDateTime inquiryDate,
        Double latitude,
        Double longitude,
        String detailedAddress,
        BigDecimal totalAmount,
        String inquiryStatus,
        String modificationDate,
        String customerId,
        String technicalId) {

}
