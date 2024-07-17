package org.recordy.server.record.service;

import org.recordy.server.record.service.dto.FileUrl;

public interface S3Service {

    // command
    FileUrl generatePresignedUrl();
}
