package com.home_fixer_hub.profile_service.Web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home_fixer_hub.profile_service.Domain.Service.NotificationService;
import com.home_fixer_hub.profile_service.Persistense.Util.TypeUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Log4j2
public class NotificationController {

    private final NotificationService notificationService;

    @PutMapping("/clients/notification/customer/{customerId}")
    public Mono<ResponseEntity<Void>> savePushTokenCustomer(@PathVariable String customerId, @RequestParam String pushToken) {
        return notificationService.savePushToken(customerId, pushToken, TypeUser.CUSTOMER.name()).map(value -> ResponseEntity.noContent().build());
    }

    @PutMapping("/technicals/notification/fixer/{technicalId}")
    public Mono<ResponseEntity<Void>> savePushTokenTechnical(@PathVariable String technicalId, @RequestParam String pushToken) {
        return notificationService.savePushToken(technicalId, pushToken, TypeUser.TECHNICAL.name()).map(value -> ResponseEntity.noContent().build());
    }

}
