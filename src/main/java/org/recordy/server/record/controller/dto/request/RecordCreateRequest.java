package org.recordy.server.record.controller.dto.request;

import org.recordy.server.record.service.dto.FileUrl;

public record RecordCreateRequest(
        String location,
        String content,
        String keywords,
        FileUrl fileUrl
) {
}
