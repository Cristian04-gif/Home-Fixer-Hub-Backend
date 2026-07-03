package com.home_fixer_hub.profile_service.Domain.Service.Imp;

import org.springframework.stereotype.Service;

import com.home_fixer_hub.profile_service.Domain.Service.NotificationService;
import com.home_fixer_hub.profile_service.Persistense.Repository.CustomerRepository;
import com.home_fixer_hub.profile_service.Persistense.Repository.TechnicalRepository;
import com.home_fixer_hub.profile_service.Persistense.Util.TypeUser;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotificationServiceImp implements NotificationService{

    private final TechnicalRepository technicalRepository;
    private final CustomerRepository customerRepository;

    @Override
    public Mono<Void> savePushToken(String id, String pushToken, String typeUser) {
        if (typeUser.equals(TypeUser.CUSTOMER.name())) {
            return customerRepository.findById(id).switchIfEmpty(Mono.error(new RuntimeException("NO se encontor al cliente con el id: "+id))).flatMap(customer -> {
                customer.setPushToken(pushToken);
                return customerRepository.save(customer);
            }).then();
        }else if (typeUser.equals(TypeUser.TECHNICAL.name())) {
            return technicalRepository.findById(id).switchIfEmpty(Mono.error(new RuntimeException("NO se encontor al tecnico con el id: "+id))).flatMap(tech -> {
                tech.setPushToken(pushToken);
                return technicalRepository.save(tech);
            }).then();
        }else{
            return Mono.error(new RuntimeException("El tipo de usuario no existe"));
        }
    }

}
