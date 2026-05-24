package com.home_fixer_hub.profile_service.Domain.Service;

import java.io.SequenceInputStream;
import java.util.Map;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dep5zhuay",
                "api_key", "613478824826563",
                "api_secret", "TnM60owVajuJDqAInSYvD_3u7_Y",
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
