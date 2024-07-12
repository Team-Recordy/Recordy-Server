package org.recordy.server.record.service.impl;

import org.recordy.server.common.config.S3Config;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.common.exception.ExternalException;
import org.recordy.server.record.service.S3Service;
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
import java.util.Objects;
import java.util.UUID;

@Component
public class S3ServiceImpl implements S3Service {

    private String bucketName;
    private S3Config s3Config;
    private S3Client s3Client;
    private static final List<String> FILE_EXTENSIONS = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/webp", "video/mp4", "video/mov", "video/quicktime");
    private static final Long MAX_SIZE = 100 * 1024 * 1024L; // 100MB

    public S3ServiceImpl(@Value("${aws-property.s3-bucket-name}") final String bucketName, S3Config s3Config) {
        this.bucketName = bucketName;
        this.s3Config = s3Config;
        this.s3Client = s3Config.getS3Client();
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        validateFileExtension(file);
        validateFileSize(file);
        final String url = getFileExtension(file);
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(url)
                .contentType(file.getContentType())
                .contentDisposition("inline")
                .build();
        RequestBody requestBody = RequestBody.fromBytes(file.getBytes());
        s3Client.putObject(request, requestBody);
        return url;
    }

    public String getFileExtension(MultipartFile file) {
        return UUID.randomUUID() + switch (Objects.requireNonNull(file.getContentType())) {
            case "image/jpeg", "image/jpg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "video/mp4" -> ".mp4";
            case "video/mov", "video/quicktime" -> ".mov";
            default -> throw new ExternalException(ErrorMessage.INVALID_FILE_TYPE);
        };
    }

    @Override
    public void deleteFile(String key) throws IOException {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
    }

    public void setS3Client(S3Client s3Client) {
        this.s3Client = s3Client;
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
