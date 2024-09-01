package org.recordy.server.record.domain.usecase;

import org.recordy.server.record.controller.dto.request.RecordCreateRequest;
import org.recordy.server.record.domain.FileUrl;

public record RecordCreate(
        long uploaderId,
        String location,
        String content,
        FileUrl fileUrl
) {

    public static RecordCreate of(Long uploaderId, RecordCreateRequest recordCreateRequest) {
        return new RecordCreate(
                uploaderId,
                recordCreateRequest.location(),
                recordCreateRequest.content(),
                recordCreateRequest.fileUrl()
        );
    }
}
