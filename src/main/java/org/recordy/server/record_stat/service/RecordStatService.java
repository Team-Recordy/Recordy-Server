package org.recordy.server.record_stat.service;

import java.util.List;
import org.recordy.server.record.domain.usecase.RecordInfoWithBookmark;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record_stat.domain.Bookmark;
import org.recordy.server.record_stat.domain.usecase.Preference;
import org.springframework.data.domain.Slice;

public interface RecordStatService {

    // command
    Bookmark bookmark(long userId, long recordId);
    void deleteBookmark(long userId, long recordId);

    // query
    Preference getPreference(long userId);
    Slice<RecordInfoWithBookmark> getBookmarkedRecordInfosWithBookmarks(long userId, long cursorId, int size);
    List<Boolean> findBookmarks(long userId, Slice<Record> records);
}
