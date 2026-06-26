package com.home_fixer_hub.booking_service.Domain.DTO.Response;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BookingResponseTech {
    private String id;
    private String serviceType;
    private String title;
    private String description;
    private LocalDateTime inquiryDate;
    private Double latitudeCustomer;
    private Double longitudeCustomer;
    private String detailedAddress;
    private Double distanceKm;
    private BigDecimal totalAmount;
    private String inquiryStatus;
    private String customerId;
    private String technicalId;
}
