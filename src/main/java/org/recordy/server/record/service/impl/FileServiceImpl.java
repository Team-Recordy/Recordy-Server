package org.recordy.server.record.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.record.exception.RecordException;
import org.recordy.server.record.service.FileService;
import org.recordy.server.record.domain.File;
import org.recordy.server.record.service.dto.FileUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private final S3Client s3Client;

    @Value("${aws-property.s3-bucket-name}")
    private String bucketName;

    @Override
    public FileUrl save(File file) {
        try {
            String videoUrl = file.videoUrl();
            String thumbnailUrl = file.thumbnailUrl();

            return new FileUrl(videoUrl, thumbnailUrl);
        } catch (Exception e) {
            throw new RecordException(ErrorMessage.FAILED_TO_UPLOAD_TO_S3);
        }
    }
}
