package org.recordy.server.record_stat.repository;

import java.util.Optional;

import org.recordy.server.record_stat.domain.Bookmark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BookmarkRepository {

    // command
    Bookmark save(Bookmark bookmark);
    void delete(long userId, long recordId);

    //query
    Slice<Bookmark> findAllByBookmarksOrderByIdDesc(long userId, long cursor, Pageable pageable);
    Optional<Bookmark> findByUserAndRecord(long userId, long recordId);
}
