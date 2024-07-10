package org.recordy.server.record.controller.dto;

import java.util.List;

public record RecordCreateRequest(
        String location,
        String content,
        List<String> keywords) {
}
