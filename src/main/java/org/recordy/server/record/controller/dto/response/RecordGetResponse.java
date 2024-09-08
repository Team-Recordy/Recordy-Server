package org.recordy.server.record.controller.dto.response;

import org.recordy.server.record.domain.FileUrl;

public record RecordGetResponse(
        Long id,
        FileUrl fileUrl,
        String content,
        Long uploaderId,
        String uploaderNickname,
        Long bookmarkCount,
        boolean isMine,
        boolean isBookmarked
) {
}
