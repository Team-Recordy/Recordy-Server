package org.recordy.server.record.controller.dto.request;

public record RecordCreateRequest(
        String location,
        String content,
        byte[] keywords) {
}
