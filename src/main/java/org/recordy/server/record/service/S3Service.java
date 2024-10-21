package org.recordy.server.record.service;

import org.recordy.server.record.domain.FileUrl;

public interface S3Service {

    // query
    FileUrl generateFilePresignedUrl();
    FileUrl convertToCloudFrontUrl(FileUrl fileUrl);
}
