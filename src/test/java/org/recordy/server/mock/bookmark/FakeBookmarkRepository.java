package org.recordy.server.mock.bookmark;

import java.util.*;

import org.recordy.server.bookmark.domain.Bookmark;
import org.recordy.server.bookmark.repository.BookmarkRepository;
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
    public void delete(long userId, long recordId) {
        bookmarks.values()
                .removeIf(bookmark ->
                        bookmark.getUser().getId() == userId && bookmark.getRecord().getId() == recordId
                );
    }

    @Override
    public Slice<Bookmark> findAllByBookmarksOrderByIdDesc(long userId, Long cursor, Pageable pageable) {
        List<Bookmark> content = bookmarks.keySet().stream()
                .filter(key -> bookmarks.get(key).getUser().getId() == userId && key < checkCursor(cursor))
                .map(bookmarks::get)
                .sorted(Comparator.comparing(Bookmark::getId).reversed())
                .toList();

        if (content.size() < pageable.getPageSize())
            return new SliceImpl<>(content, pageable, false);

        return new SliceImpl<>(content.subList(0, pageable.getPageSize()), pageable, true);
    }

    private Long checkCursor(Long cursor) {
        if (cursor != null) {
            return cursor;
        }

        return Long.MAX_VALUE;
    }

    @Override
    public boolean existsByUserIdAndRecordId(Long userId, Long recordId) {
        return bookmarks.values().stream()
                .anyMatch(bookmark ->
                        bookmark.getUser().getId().equals(userId) && bookmark.getRecord().getId().equals(recordId)
                );
    }

    @Override
    public void deleteByUserId(long userId) {
        bookmarks.values()
                .removeIf(bookmark -> bookmark.getUser().getId() == userId);
    }

    @Override
    public long countByUserId(Long userId) {
        return bookmarks.values().stream()
                .filter(bookmark -> bookmark.getUser().getId() == userId)
                .count();
    }
}
