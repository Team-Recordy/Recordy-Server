package org.recordy.server.external.service.impl;

import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.external.config.S3Config;
import org.recordy.server.external.exception.ExternalException;
import org.recordy.server.external.service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class S3ServiceImpl implements S3Service {

    private final String bucketName;
    private final S3Config s3Config;
    private static final List<String> FILE_EXTENSIONS = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/webp","video/mp4", "video/mov", "video/quicktime");
    private static final Long MAX_SIZE = 100 * 1024 * 1024L; // 100MB

    public S3ServiceImpl(@Value("${aws-property.s3-bucket-name}") final String bucketName, S3Config s3Config) {
        this.bucketName = bucketName;
        this.s3Config = s3Config;
    }

    @Override
    public String uploadImage(MultipartFile image) throws IOException {
        final String key = generateImageFileName();
        final S3Client s3Client = s3Config.getS3Client();

        validateFileExtension(image);
        validateFileSize(image);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(image.getContentType())
                .contentDisposition("inline")
                .build();

        RequestBody requestBody = RequestBody.fromBytes(image.getBytes());
        s3Client.putObject(request, requestBody);
        return key;
    }

    @Override
    public String uploadVideo(MultipartFile video) throws IOException {
        final String key = generateVideoFileName();
        final S3Client s3Client = s3Config.getS3Client();

        validateFileExtension(video);
        validateFileSize(video);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(video.getContentType())
                .contentDisposition("inline")
                .build();

        RequestBody requestBody = RequestBody.fromBytes(video.getBytes());
        s3Client.putObject(request, requestBody);
        return key;
    }

    @Override
    public void deleteFile(String key) throws IOException {
        final S3Client s3Client = s3Config.getS3Client();

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
    }

    private String generateImageFileName() {
        return UUID.randomUUID() + ".jpg";
    }

    private String generateVideoFileName() {
        return UUID.randomUUID() + ".mp4";
    }


    public void validateFileExtension(MultipartFile file) {
        String contentType = file.getContentType();
        if (!FILE_EXTENSIONS.contains(contentType)) {
            throw new ExternalException(ErrorMessage.INVALID_FILE_TYPE);
        }
    }

    public void validateFileSize(MultipartFile file) {
        if (file.getSize() > MAX_SIZE) {
            throw new ExternalException(ErrorMessage.INVALID_FILE_SIZE);
        }
    }

}
