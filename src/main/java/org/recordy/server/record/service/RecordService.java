package org.recordy.server.record.service;

import org.recordy.server.record.domain.usecase.RecordInfoWithBookmark;
import org.recordy.server.record.domain.File;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.usecase.RecordCreate;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface RecordService {

    // command
    Record create(RecordCreate recordCreate, File file);
    void delete(long userId, long recordId);

    // query
    void watch(long userId, long recordId);
    Slice<RecordInfoWithBookmark> getFamousRecordInfosWithBookmarks(long userId, List<String> keywords, int pageNumber, int size);
    Slice<RecordInfoWithBookmark> getRecentRecordInfosWithBookmarksByUser(long userId, long cursorId, int size);
    Slice<RecordInfoWithBookmark> getRecentRecordInfosWithBookmarks(long userId, List<String> keywords, Long cursorId, int size);
    Slice<RecordInfoWithBookmark> getSubscribingRecordInfosWithBookmarks(long userId, long cursorId, int size);
}
