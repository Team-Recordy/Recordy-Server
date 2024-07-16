package org.recordy.server.record.controller.dto.response;

import org.recordy.server.record.service.dto.FileUrl;
import org.recordy.server.record.domain.Record;
public record RecordInfo (
        Long id,
        FileUrl fileUrl,
        String location,
        String content,
        Long uploaderId,
        String uploaderNickname,
        Long bookmarkCount,
        boolean isMine
){
    public static RecordInfo from(Record record, Long currentUserId){
        return new RecordInfo(
                record.getId(),
                record.getFileUrl(),
                record.getLocation(),
                record.getContent(),
                record.getUploader().getId(),
                record.getUploader().getNickname(),
                record.getBookmarkCount(),
                record.getUploader().getId().equals(currentUserId)
        );
    }
}
