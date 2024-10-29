package org.recordy.server.record.controller.dto.request;

import org.recordy.server.record.domain.FileUrl;

public record RecordCreateRequest(
        FileUrl fileUrl,
        String content,
        String exhibitionName,
        Long placeId
) {
}
