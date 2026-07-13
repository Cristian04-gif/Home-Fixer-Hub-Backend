package com.home_fixer_hub.catalog_service.Domain.Service;

import java.io.SequenceInputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.cloud_name}") String cloudName,
            @Value("${cloudinary.api_key}") String apiKey,
            @Value("${cloudinary.api_secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true));
    }

    public Mono<String> uploadImageCloud(FilePart filePart) {
        return filePart.content()
                .map(dataBuffer -> dataBuffer.asInputStream(true))
                .reduce(SequenceInputStream::new)
                .flatMap(inputStream -> Mono.fromCallable(() -> {
                    byte[] fileBytes = inputStream.readAllBytes();

                    Map<?, ?> uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.emptyMap());
                    return (String) uploadResult.get("secure_url");
                })).subscribeOn(Schedulers.boundedElastic());
    }
}
