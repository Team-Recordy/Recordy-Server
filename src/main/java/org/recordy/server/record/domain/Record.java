package org.recordy.server.record.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.record.service.dto.FileUrl;
import org.recordy.server.user.domain.User;

@AllArgsConstructor
@Builder
@Getter
public class Record {

    String videoUrl;
    String thumbnailUrl;
    String location;
    String content;
    User uploader;

    public static Record of(RecordCreate recordCreate, FileUrl fileUrl, User uploader) {
        return new Record(
                fileUrl.videoUrl(),
                fileUrl.thumbnailUrl(),
                recordCreate.location(),
                recordCreate.content(),
                uploader
        );
    }
}
