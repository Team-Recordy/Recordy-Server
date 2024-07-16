package org.recordy.server.bookmark.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.recordy.server.bookmark.service.BookmarkService;
import org.recordy.server.common.dto.response.CursorBasePaginatedResponse;
import org.recordy.server.auth.security.resolver.UserId;
import org.recordy.server.record.controller.dto.response.RecordInfoWithBookmark;
import org.recordy.server.record.domain.Record;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
@RestController
public class BookmarkController implements BookmarkApi {

    private final BookmarkService bookmarkService;

    @Override
    @PostMapping("/{recordId}")
    public ResponseEntity<Boolean> bookmark(
            @UserId Long userId,
            @PathVariable Long recordId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookmarkService.bookmark(userId, recordId));
    }
}
