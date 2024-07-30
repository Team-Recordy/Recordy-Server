package org.recordy.server.bookmark.service;

import java.util.List;
import org.recordy.server.bookmark.domain.Bookmark;
import org.recordy.server.record.domain.Record;
import org.springframework.data.domain.Slice;

public interface BookmarkService {

    // command
    boolean bookmark(long userId, long recordId);

    // query
    List<Boolean> findBookmarks(long userId, List<Record> records);
    Long countBookmarks(long userId);
    Slice<Bookmark> getBookmarks(long userId, long cursorId, int size);
}
