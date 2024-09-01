package org.recordy.server.record.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.recordy.server.record.service.dto.FileUrl;
import org.recordy.server.user.domain.User;

@AllArgsConstructor
@Builder
@Getter
public class Record {

    Long id;
    FileUrl fileUrl;
    String location;
    String content;
    User uploader;
    private LocalDateTime createdAt;
    long bookmarkCount;

    public boolean isUploader(long userId) {
        return uploader.getId() == userId;
    }
}
