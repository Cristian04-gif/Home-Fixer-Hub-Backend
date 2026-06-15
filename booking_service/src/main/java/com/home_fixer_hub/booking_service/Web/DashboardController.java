package com.home_fixer_hub.booking_service.Web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home_fixer_hub.booking_service.Domain.DTO.DashboardTechnical;
import com.home_fixer_hub.booking_service.Domain.Service.DashboardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/bookings/dashboard")
@Log4j2
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/fixer/{technicalId}")
    public Mono<ResponseEntity<DashboardTechnical>> dahsboardtechnical(@PathVariable String technicalId) {
        System.out.println("controlador");
        return dashboardService.dashboardTechnical(technicalId).map(value -> ResponseEntity.ok(value))
                .onErrorResume(e -> {
                    log.error("no se pudo tener el dashboar del tecnico, {}", e);
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

}
