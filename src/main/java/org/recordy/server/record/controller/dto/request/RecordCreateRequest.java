package org.recordy.server.record.controller.dto.request;

import org.recordy.server.record.service.dto.FileUrl;

import java.util.List;

public record RecordCreateRequest(
        String location,
        String content,
        List<String> keywords,
        FileUrl fileUrl) {
}
