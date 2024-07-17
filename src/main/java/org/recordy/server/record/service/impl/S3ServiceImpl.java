package org.recordy.server.record.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.record.service.S3Service;
import org.recordy.server.record.service.dto.FileUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    @Value("${aws-property.s3-bucket-name}")
    private String bucket;

    private final S3Presigner presigner;

    @Override
    public FileUrl generatePresignedUrl() {
        return new FileUrl(
                generatePresignedUrl("videos/", ".mp4"),
                generatePresignedUrl("thumbnails/", ".jpg")
        );
    }

    private String generatePresignedUrl(String directory, String extension) {
        String fileName = directory + UUID.randomUUID() + extension;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);

        return presignedRequest.url().toString();
    }
}
