package org.recordy.server.record_stat.service;

import org.recordy.server.record.domain.Record;
import org.recordy.server.record_stat.domain.Bookmark;
import org.recordy.server.record_stat.domain.usecase.Preference;
import org.springframework.data.domain.Slice;

public interface RecordStatService {

    // command
    Bookmark bookmark(long userId, long recordId);

    // query
    Preference getPreference(long userId);
    Slice<Record> getBookmarkedRecords(long userId, long cursorId, int size);
    long getBookmarkCount(long recordId);
}
