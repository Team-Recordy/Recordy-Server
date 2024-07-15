package org.recordy.server.record.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.service.dto.FileUrl;
import org.recordy.server.user.domain.User;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class Record {

    Long id;
    @Column(length = 1024)
    FileUrl fileUrl;
    String location;
    String content;
    List<Keyword> keywords;
    User uploader;
    private LocalDateTime createdAt;
    long bookmarkCount;

    public boolean isUploader(long userId) {
        return uploader.getId() == userId;
    }
}
