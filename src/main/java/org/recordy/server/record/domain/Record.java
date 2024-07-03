package org.recordy.server.record.domain;

import lombok.AllArgsConstructor;
import org.recordy.server.record.domain.usecase.RecordCreate;

@AllArgsConstructor
public class Record {

    String videoUrl;
    String thumbnailUrl;
    String location;
    String content;

    public static Record from(RecordCreate recordCreate) {
        return new Record(
                recordCreate.videoUrl(),
                recordCreate.thumbnailUrl(),
                recordCreate.location(),
                recordCreate.content()
        );
    }
}
