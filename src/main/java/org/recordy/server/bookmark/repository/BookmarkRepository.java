package org.recordy.server.bookmark.repository;

import org.recordy.server.bookmark.domain.Bookmark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BookmarkRepository {
    // command
    void save(Bookmark bookmark);
    void delete(long userId, long recordId);
    void deleteByUserId(long userId);

    // query
    Bookmark findById(Long id);
    Slice<Bookmark> findAllByBookmarksOrderByIdDesc(long userId, Long cursor, Pageable pageable);
    boolean existsByUserIdAndRecordId(Long userId, Long recordId);
    long countByUserId(Long userId);
}
