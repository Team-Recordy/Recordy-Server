package org.recordy.server.record.domain.usecase;

public record RecordCreate(
        long uploaderId,
        String videoUrl,
        String thumbnailUrl,
        String location,
        String content
) {
}
