package org.recordy.server.record.domain.usecase;

import org.recordy.server.record.service.keyword.domain.Keyword;
import org.recordy.server.record.controller.dto.RecordCreateRequest;

import java.util.List;
import java.util.stream.Collectors;

public record RecordCreate(
        long uploaderId,
        String location,
        String content,
        List<Keyword> keywords
) {

    public static RecordCreate of(Long uploaderId, String location, String content, List<Keyword> keywords){
        return new RecordCreate(uploaderId, location, content, keywords);
    }

    public static RecordCreate from(Long uploaderId, RecordCreateRequest recordCreateRequest) {
        List<Keyword> keywords = recordCreateRequest.keywords().stream()
                .map(Keyword::valueOf)
                .collect(Collectors.toList());
        return new RecordCreate(uploaderId, recordCreateRequest.location(), recordCreateRequest.content(), keywords);
    }



}
