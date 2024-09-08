package org.recordy.server.util;

import org.recordy.server.bookmark.domain.Bookmark;
import org.recordy.server.record.domain.Record;
import org.recordy.server.user.domain.User;

public class BookmarkFixture {

    public static Bookmark create(User user, Record record) {
        return Bookmark.builder()
                .user(user)
                .record(record)
                .build();
    }
}
