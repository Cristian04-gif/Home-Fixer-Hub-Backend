package com.home_fixer_hub.review_service.Domain.Service.Imp;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.home_fixer_hub.review_service.Domain.Client.BookingClient;
import com.home_fixer_hub.review_service.Domain.Client.TechnicalClient;
import com.home_fixer_hub.review_service.Domain.DTO.BookingDTO;
import com.home_fixer_hub.review_service.Domain.DTO.ReviewRequest;
import com.home_fixer_hub.review_service.Domain.DTO.TechnicalDTO;
import com.home_fixer_hub.review_service.Domain.Service.ReviewService;
import com.home_fixer_hub.review_service.Persistense.Mapping.ReviewMapper;
import com.home_fixer_hub.review_service.Persistense.Models.Review;
import com.home_fixer_hub.review_service.Persistense.Repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReviewServiceImp implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final TechnicalClient technicalClient;
    private final BookingClient bookingClient;

    @Override
    public Mono<Void> register(ReviewRequest reviewRequest) {
        Mono<TechnicalDTO> tech = technicalClient.getTechnicalById(reviewRequest.technicalId())
                .switchIfEmpty(Mono.error(
                        new RuntimeException("No se encontro el tecnico con el id: " + reviewRequest.technicalId())));

        Mono<BookingDTO> booking = bookingClient.getJobById(reviewRequest.bookingId())
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontro el servicio que se quiere valorar")));

        return Mono.zip(booking, tech).flatMap(tuple -> {
            BookingDTO book = tuple.getT1();
            TechnicalDTO technical = tuple.getT2();

            Review review = reviewMapper.toEntity(reviewRequest);
            review.setFechaPublicacion(LocalDate.now());

            return reviewRepository.save(review).flatMap(saveReview -> {
                Mono<Integer> sumaMono = reviewRepository.findByIdTecnico(technical.getId()).map(r -> r.getPuntuacion())
                        .reduce(0, Integer::sum);

                Mono<Long> cantidadMono = reviewRepository.countByIdTecnico(technical.getId());

                return Mono.zip(sumaMono, cantidadMono).flatMap(value -> {
                    Integer sumPuntos = value.getT1();
                    Long cantPuntos = value.getT2();
                    double promedio = (cantPuntos > 0) ? (sumPuntos / cantPuntos) : 0.0;

                    promedio = Math.round(promedio * 10.0) / 10.0;
                    return technicalClient.saveRatingTechnical(technical.getId(), book.id(), promedio);

                });
            });

        }).then();
    }

    @Override
    public Mono<Review> getById(String reviewId) {
        return reviewRepository.findById(reviewId)
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontro la valoracion con el id: " + reviewId)));
    }

    @Override
    public Flux<Review> getByTechnical(String technicalId) {
        return technicalClient.getTechnicalById(technicalId).switchIfEmpty(Mono.error(
                new RuntimeException("No se encontro el tecnico con el id: " + technicalId)))
                .flatMapMany(tech -> reviewRepository.findByIdTecnico(technicalId));
    }

}
