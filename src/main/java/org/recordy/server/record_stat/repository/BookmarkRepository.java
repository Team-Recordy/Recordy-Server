package org.recordy.server.record_stat.repository;

import org.recordy.server.record_stat.domain.Bookmark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.recordy.server.record.domain.Record;

import java.util.List;

public interface BookmarkRepository {
    // command
    Bookmark save(Bookmark bookmark);
    void delete(long userId, long recordId);

    // query
    Slice<Bookmark> findAllByBookmarksOrderByIdDesc(long userId, long cursor, Pageable pageable);
    boolean existsByUserIdAndRecordId(Long userId, Long recordId);
    List<Boolean> findBookmarks(long userId, Slice<Record> records);
}
