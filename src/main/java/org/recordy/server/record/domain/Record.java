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
    boolean isBlocked;

    private Record(
            Long id,
            User uploader,
            Place place,
            FileUrl fileUrl,
            String exhibitionName,
            String content
    ) {
        this.id = id;
        this.uploader = uploader;
        this.place = place;
        this.fileUrl = fileUrl;
        this.exhibitionName = exhibitionName;
        this.content = content;
    }

    public static Record from(RecordEntity entity) {
        return new Record(
                entity.getId(),
                User.from(entity.getUser()),
                Place.from(entity.getPlace()),
                entity.getFileUrl(),
                entity.getExhibitionName(),
                entity.getContent()
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
                0,
                false
        );
    }

    public boolean isUploader(long userId) {
        return uploader.getId() == userId;
    }
}
