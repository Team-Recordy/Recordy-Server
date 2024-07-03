package org.recordy.server.record.service.dto;

import org.springframework.web.multipart.MultipartFile;

public record File(
        MultipartFile video,
        MultipartFile thumbnail
) {
}
