package org.recordy.server.record.domain.usecase;

import org.recordy.server.place.domain.Place;
import org.recordy.server.record.domain.FileUrl;
import org.recordy.server.user.domain.User;

public record RecordCreate(
        Long id,
        FileUrl fileUrl,
        String content,
        User uploader,
        Place place
) {
}
