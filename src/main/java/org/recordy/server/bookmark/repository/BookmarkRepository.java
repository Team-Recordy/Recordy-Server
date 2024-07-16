package org.recordy.server.bookmark.repository;

import org.recordy.server.bookmark.domain.Bookmark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BookmarkRepository {
    // command
    Bookmark save(Bookmark bookmark);
    void delete(long userId, long recordId);

    // query
    Slice<Bookmark> findAllByBookmarksOrderByIdDesc(long userId, long cursor, Pageable pageable);
    boolean existsByUserIdAndRecordId(Long userId, Long recordId);
}
