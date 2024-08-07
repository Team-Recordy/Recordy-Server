package org.recordy.server.record.controller.dto.response;

import org.recordy.server.bookmark.domain.Bookmark;
import org.springframework.data.domain.Slice;

public record BookmarkedRecord (
        Long bookmarkId,
        RecordInfo recordInfo,
        Boolean isBookmark
) {
    public static Slice<BookmarkedRecord> of(Slice<Bookmark> bookmarks, Long currentUserId) {
        return bookmarks.map(bookmark -> new BookmarkedRecord(bookmark.getId(), RecordInfo.from(bookmark.getRecord(), currentUserId), true));
    }
}

