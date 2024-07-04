package org.recordy.server.record.domain.usecase;

import org.recordy.server.keyword.domain.Keyword;

import java.util.List;

public record RecordCreate(
        long uploaderId,
        String location,
        String content,
        List<Keyword> keywords
) {
}
