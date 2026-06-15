package com.home_fixer_hub.booking_service.Domain.Service;

import com.home_fixer_hub.booking_service.Domain.DTO.DashboardTechnical;

import reactor.core.publisher.Mono;

public interface DashboardService {
    public Mono<DashboardTechnical> dashboardTechnical(String technicalId);
}
