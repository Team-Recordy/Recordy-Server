package org.recordy.server.mock.bookmark;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record_stat.domain.Bookmark;
import org.recordy.server.record_stat.repository.BookmarkRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class FakeBookmarkRepository implements BookmarkRepository {
    public long autoIncrementId = 1L;
    public final Map<Long, Bookmark> bookmarks = new HashMap<>();

    @Override
    public Bookmark save(Bookmark bookmark) {
        Bookmark realBookmark = Bookmark.builder()
                .id(autoIncrementId)
                .user(bookmark.getUser())
                .record(bookmark.getRecord())
                .build();

        bookmarks.put(autoIncrementId++, realBookmark);

        return realBookmark;
    }

    @Override
    public Slice<Bookmark> findAllByBookmarksOrderByIdDesc(long userId, long cursor, Pageable pageable) {
        List<Bookmark> content = bookmarks.keySet().stream()
                .filter(key -> bookmarks.get(key).getUser().getId() == userId && key < cursor)
                .map(bookmarks::get)
                .sorted(Comparator.comparing(Bookmark::getId).reversed())
                .toList();

        if (content.size() < pageable.getPageSize())
            return new SliceImpl<>(content, pageable, false);

        return new SliceImpl<>(content.subList(0, pageable.getPageSize()), pageable, true);
    }

    @Override
    public Map<Keyword, Long> countAllByUserIdGroupByKeyword(long userId) {
        return Map.of();
    }
}
