package org.recordy.server.record.controller.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import org.recordy.server.bookmark.domain.Bookmark;
import org.recordy.server.record.domain.Record;
import org.springframework.data.domain.Slice;

public record BookmarkedRecord (
        Long bookmarkId,
        RecordInfo recordInfo,
        Boolean isBookmark
){
    public static Slice<BookmarkedRecord> of (Slice<Bookmark> bookmarks, Long currentUserId) {
        return bookmarks.map(bookmark ->  new BookmarkedRecord(bookmark.getId(), RecordInfo.from(bookmark.getRecord(), currentUserId), true));
    }
}

