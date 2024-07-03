package org.recordy.server.record.domain.usecase;

public record RecordCreate(
        long uploaderId,
        String location,
        String content
) {
}
