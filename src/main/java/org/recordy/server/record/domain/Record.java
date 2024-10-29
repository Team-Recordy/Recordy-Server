package org.recordy.server.record.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.recordy.server.place.domain.Place;
import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.user.domain.User;

@AllArgsConstructor
@Builder
@Getter
public class Record {

    Long id;
    FileUrl fileUrl;
    String content;
    String exhibitionName;
    User uploader;
    Place place;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    long bookmarkCount;

    private Record(
            Long id,
            FileUrl fileUrl,
            String content,
            String exhibitionName,
            User uploader,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            long bookmarkCount
    ) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.content = content;
        this.exhibitionName = exhibitionName;
        this.uploader = uploader;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.bookmarkCount = bookmarkCount;
    }

    private Record(
            Long id,
            User uploader
    ) {
        this.id = id;
        this.uploader = uploader;
    }

    public static Record from(RecordEntity entity) {
        return new Record(
                entity.getId(),
                User.from(entity.getUser())
        );
    }

    public static Record create(RecordCreate create) {
        return new Record(
                create.id(),
                create.fileUrl(),
                create.content(),
                create.exhibitionName(),
                create.uploader(),
                create.place(),
                null,
                null,
                0
        );
    }

    public boolean isUploader(long userId) {
        return uploader.getId() == userId;
    }
}
