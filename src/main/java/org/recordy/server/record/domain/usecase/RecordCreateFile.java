package org.recordy.server.record.domain.usecase;

import org.springframework.web.multipart.MultipartFile;

public record RecordCreateFile(
        long uploaderId,
        MultipartFile video,
        MultipartFile thumbnail,
        String location,
        String content
) {
}
