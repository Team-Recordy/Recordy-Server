package org.recordy.server.record_stat.repository;

import java.util.Optional;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record_stat.domain.Bookmark;
import org.recordy.server.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Map;

public interface BookmarkRepository {

    // command
    Bookmark save(Bookmark bookmark);
    void deleteById(long bookmarkId);

    //query
    Slice<Bookmark> findAllByBookmarksOrderByIdDesc(long userId, long cursor, Pageable pageable);
    Optional<Bookmark> findByUserIdAndRecordId(long userId, long recordId);
}
