package org.recordy.server.record.domain.usecase;

import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.controller.dto.request.RecordCreateRequest;
import org.recordy.server.record.service.dto.FileUrl;

import java.util.List;

public record RecordCreate(
        long uploaderId,
        String location,
        String content,
        List<Keyword> keywords,
        FileUrl fileUrl
) {

    public static RecordCreate of(Long uploaderId, RecordCreateRequest recordCreateRequest) {
        return new RecordCreate(
                uploaderId,
                recordCreateRequest.location(),
                recordCreateRequest.content(),
                Keyword.decode(recordCreateRequest.keywords()),
                recordCreateRequest.fileUrl()
        );
    }
}
