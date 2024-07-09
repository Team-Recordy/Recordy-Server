package org.recordy.server.record.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.external.service.S3Service;
import org.recordy.server.record.exception.RecordException;
import org.recordy.server.record.service.FileService;
import org.recordy.server.record.domain.File;
import org.recordy.server.record.service.dto.FileUrl;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private final S3Service s3Service;

    @Override
    public FileUrl save(File file) {
        try {
            String videoUrl = s3Service.uploadFile(file.video());
            String thumbnailUrl = s3Service.uploadFile(file.thumbnail());

            return new FileUrl(videoUrl, thumbnailUrl);
        } catch (IOException e) {
            throw new RecordException(ErrorMessage.FAILED_TO_UPLOAD_TO_S3);
        }

        return null;
    }
}
