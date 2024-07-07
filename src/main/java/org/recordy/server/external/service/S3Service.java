package org.recordy.server.external.service;

import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.external.config.AwsConfig;
import org.recordy.server.external.exception.ExternalException;
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
public class S3Service {

    private final String bucketName;
    private final AwsConfig awsConfig;
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/webp");
    private static final List<String> VIDEO_EXTENSIONS = Arrays.asList("video/mp4");

    public S3Service(@Value("${aws-property.s3-bucket-name}") final String bucketName, AwsConfig awsConfig) {
        this.bucketName = bucketName;
        this.awsConfig = awsConfig;
    }

    public String uploadImage(String directoryPath, MultipartFile image) throws IOException {
        final String key = directoryPath + generateImageFileName();
        final S3Client s3Client = awsConfig.getS3Client();

        validateImageExtension(image);
        validateImageSize(image);

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

    public String uploadVideo(String directoryPath, MultipartFile video) throws IOException {
        final String key = directoryPath + generateVideoFileName();
        final S3Client s3Client = awsConfig.getS3Client();

        validateVideoExtension(video);
        validateVideoSize(video);

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
    public void deleteFile(String key) throws IOException {
        final S3Client s3Client = awsConfig.getS3Client();

        s3Client.deleteObject((DeleteObjectRequest.Builder builder) ->
                builder.bucket(bucketName)
                        .key(key)
                        .build()
        );
    }

    private String generateImageFileName() {
        return UUID.randomUUID() + ".jpg";
    }

    private String generateVideoFileName() {
        return UUID.randomUUID() + ".mp4";
    }

    private void validateImageExtension(MultipartFile image) {
        String contentType = image.getContentType();
        if (!IMAGE_EXTENSIONS.contains(contentType)) {
            throw new ExternalException(ErrorMessage.INVALID_IMAGE_TYPE);
        }
    }

    private void validateVideoExtension(MultipartFile video) {
        String contentType = video.getContentType();
        if (!VIDEO_EXTENSIONS.contains(contentType)) {
            throw new ExternalException(ErrorMessage.INVALID_VIDEO_TYPE);
        }
    }

    private static final Long MAX_IMAGE_SIZE = 5 * 1024 * 1024L; //5MB
    private static final Long MAX_VIDEO_SIZE = 100 * 1024 * 1024L; //100MB
    private void validateImageSize(MultipartFile image) {
        if (image.getSize() > MAX_IMAGE_SIZE) {
            throw new ExternalException(ErrorMessage.INVALID_IMAGE_FORMAT);
        }
    }

    private void validateVideoSize(MultipartFile video) {
        if (video.getSize() > MAX_VIDEO_SIZE) {
            throw new ExternalException(ErrorMessage.INVALID_VIDEO_FORMAT);
        }
    }
}
