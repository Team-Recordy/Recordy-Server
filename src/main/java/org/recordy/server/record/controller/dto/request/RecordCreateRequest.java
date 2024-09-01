package org.recordy.server.record.controller.dto.request;

import org.recordy.server.record.domain.FileUrl;

public record RecordCreateRequest(
        String location,
        String content,
        FileUrl fileUrl
) {
}
