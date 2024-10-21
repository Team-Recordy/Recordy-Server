package org.recordy.server.record.service.impl;

import org.recordy.server.record.service.S3Service;
import org.recordy.server.record.domain.FileUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;
import java.util.UUID;

@Component
public class S3ServiceImpl implements S3Service {

    private final String bucket;
    private final String cloudFrontDomain;
    private final String s3Domain;
    private final S3Presigner presigner;

    public S3ServiceImpl(
            S3Presigner presigner,
            @Value("${aws-property.s3-domain}") String s3Domain,
            @Value("${aws-property.cloudfront-domain-name}") String cloudFrontDomain,
            @Value("${aws-property.s3-bucket-name}") String bucket
    ) {
        this.presigner = presigner;
        this.s3Domain = s3Domain;
        this.cloudFrontDomain = cloudFrontDomain;
        this.bucket = bucket;
    }

    @Override
    public String generateProfileImageUrl() {
        return generatePresignedUrl("profile-images/", ".jpeg");
    }

    @Override
    public FileUrl generateFilePresignedUrl() {
        return new FileUrl(
                generatePresignedUrl("videos/", ".mp4"),
                generatePresignedUrl("thumbnails/", ".jpeg")
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

    @Override
    public FileUrl convertToCloudFrontUrl(FileUrl fileUrl) {
        String cloudFrontVideoUrl = fileUrl.videoUrl().replace(s3Domain, cloudFrontDomain);
        String cloudFrontThumbnailUrl = fileUrl.thumbnailUrl().replace(s3Domain, cloudFrontDomain);

        return FileUrl.of(cloudFrontVideoUrl, cloudFrontThumbnailUrl);
    }
}
