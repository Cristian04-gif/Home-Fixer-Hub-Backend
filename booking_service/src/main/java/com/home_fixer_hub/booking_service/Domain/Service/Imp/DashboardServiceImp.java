package com.home_fixer_hub.booking_service.Domain.Service.Imp;

import org.springframework.stereotype.Service;

import com.home_fixer_hub.booking_service.Domain.Client.TechnicalClient;
import com.home_fixer_hub.booking_service.Domain.DTO.DashboardTechnical;
import com.home_fixer_hub.booking_service.Domain.DTO.TechnicalDTO;
import com.home_fixer_hub.booking_service.Domain.Service.BookingService;
import com.home_fixer_hub.booking_service.Domain.Service.DashboardService;
import com.home_fixer_hub.booking_service.Persistense.Mapping.BookingMapper;
import com.home_fixer_hub.booking_service.Persistense.Model.BookingStatus;
import com.home_fixer_hub.booking_service.Persistense.Repository.BookingRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DashboardServiceImp implements DashboardService {

        private final TechnicalClient technicalClient;
        private final BookingRepository bookingRepository;
        private final BookingService bookingService;
        private final BookingMapper bookingMapper;

        @Override
        public Mono<DashboardTechnical> dashboardTechnical(String technicalId) {
                System.out.println("dhasboard tecnico");
                Mono<TechnicalDTO> technical = technicalClient.getTechnicalById(technicalId)
                                .switchIfEmpty(Mono.error(new RuntimeException(
                                                "No se encontro al tecnico con el id: " + technicalId)));

                Mono<Long> pendingQueries = bookingRepository.countByIdTecnicoAndEstadoConsulta(technicalId,
                                BookingStatus.PENDIENTE.toString())
                                .switchIfEmpty(Mono.just(0L));
                Mono<Long> aceeptQueries = bookingRepository.countByIdTecnicoAndEstadoConsulta(technicalId,
                                BookingStatus.ACEPTADA.toString())
                                .switchIfEmpty(Mono.just(0L));

                Mono<Long> processRequests = bookingRepository.countByIdTecnicoAndEstadoConsulta(technicalId,
                                BookingStatus.EN_PROCESO.toString())
                                .switchIfEmpty(Mono.just(0L));
                Mono<Long> completeRequests = bookingRepository.countByIdTecnicoAndEstadoConsulta(technicalId,
                                BookingStatus.FINALIZADA.toString())
                                .switchIfEmpty(Mono.just(0L));
                return Mono.zip(technical, pendingQueries, aceeptQueries, processRequests, completeRequests)
                                .flatMap(tuple -> {
                                        DashboardTechnical dashboardTechnical = DashboardTechnical.builder()
                                                        .pendingQueries(tuple.getT2())
                                                        .processRequests(tuple.getT3() + tuple.getT4())
                                                        .completeRequests(tuple.getT5())
                                                        .averageRanking(tuple.getT1().averageRating())
                                                        .available(tuple.getT1().available())
                                                        .build();
                                        return Mono.just(dashboardTechnical);
                                });
        }

}
