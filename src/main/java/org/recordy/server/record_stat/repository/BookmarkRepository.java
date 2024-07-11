package org.recordy.server.record_stat.repository;

import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record_stat.domain.Bookmark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Map;

public interface BookmarkRepository {

    // command
    Bookmark save(Bookmark bookmark);

    //query
    Slice<Bookmark> findAllByBookmarksOrderByIdDesc(long userId, long cursor, Pageable pageable);
    Map<Keyword, Long> countAllByUserIdGroupByKeyword(long userId);
}
