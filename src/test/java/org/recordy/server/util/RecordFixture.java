package org.recordy.server.util;

import org.recordy.server.place.domain.Place;
import org.recordy.server.record.domain.FileUrl;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.user.domain.User;

public class RecordFixture {

    public static final FileUrl FILE_URL = new FileUrl("", "");
    public static final String CONTENT = "content";
    public static final User UPLOADER = DomainFixture.createUser();
    public static final Place PLACE = PlaceFixture.create();

    public static Record create() {
        return Record.create(new RecordCreate(
                null,
                FILE_URL,
                CONTENT,
                UPLOADER,
                PLACE
        ));
    }

    public static Record create(Long id) {
        return Record.create(new RecordCreate(
                id,
                FILE_URL,
                CONTENT,
                UPLOADER,
                PLACE
        ));
    }

    public static Record create(Place place) {
        return Record.create(new RecordCreate(
                null,
                FILE_URL,
                CONTENT,
                UPLOADER,
                place
        ));
    }
}
