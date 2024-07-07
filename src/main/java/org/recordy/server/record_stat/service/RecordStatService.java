package org.recordy.server.record_stat.service;

import org.recordy.server.record_stat.domain.Bookmark;
import org.recordy.server.record_stat.domain.usecase.Preference;
import org.springframework.data.domain.Slice;

public interface RecordStatService {

    // command
    Bookmark bookmark(long userId, long postId);

    // query
    Preference getPreference(long userId);
    Slice<Bookmark> getBookmarks(long userId, long cursorId, int size);
    long getBookmarkCount(long recordId);
}
