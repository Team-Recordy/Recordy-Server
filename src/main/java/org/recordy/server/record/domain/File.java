package org.recordy.server.record.domain;

import org.springframework.web.multipart.MultipartFile;

public record File(
        MultipartFile video,
        MultipartFile thumbnail
) {
}
