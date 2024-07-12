package org.recordy.server.record.controller.dto.request;

import java.util.List;

public record RecordCreateRequest(
        String location,
        String content,
        List<String> keywords) {
}
