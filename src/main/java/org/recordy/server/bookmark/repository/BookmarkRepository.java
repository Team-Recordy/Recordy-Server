package org.recordy.server.bookmark.repository;

import org.recordy.server.bookmark.domain.Bookmark;
import org.recordy.server.keyword.domain.Keyword;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Map;

public interface BookmarkRepository {
    // command
    Bookmark save(Bookmark bookmark);
    void delete(long userId, long recordId);
    void deleteByUserId(long userId);

    // query
    Slice<Bookmark> findAllByBookmarksOrderByIdDesc(long userId, long cursor, Pageable pageable);
    boolean existsByUserIdAndRecordId(Long userId, Long recordId);
    long countByUserId(Long userId);
    Map<Keyword, Long> countAllByUserIdGroupByKeyword(long userId);
}
