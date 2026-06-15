package com.home_fixer_hub.booking_service.Domain.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DashboardTechnical {
    private Long pendingQueries;
    private Long processRequests;
    private Long completeRequests;
    private BigDecimal incomes;
    private Double averageRanking;
    private Boolean available;
}
