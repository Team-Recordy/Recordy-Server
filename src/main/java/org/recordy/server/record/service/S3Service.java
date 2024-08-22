package org.recordy.server.record.service;

import org.recordy.server.record.service.dto.FileUrl;

public interface S3Service {

    // query
    FileUrl generatePresignedUrl();
    FileUrl convertToCloudFrontUrl(FileUrl fileUrl);
}
