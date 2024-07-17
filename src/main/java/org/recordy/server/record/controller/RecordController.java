package org.recordy.server.record.controller;

import lombok.RequiredArgsConstructor;
import org.recordy.server.bookmark.service.BookmarkService;
import org.recordy.server.common.dto.response.CursorBasePaginatedResponse;
import org.recordy.server.common.dto.response.PaginatedResponse;
import org.recordy.server.auth.security.resolver.UserId;
import org.recordy.server.record.controller.dto.request.RecordCreateRequest;
import org.recordy.server.record.controller.dto.response.RecordInfoWithBookmark;
import org.recordy.server.record.domain.File;
import org.recordy.server.record.domain.Record;

import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.record.service.RecordService;
import org.recordy.server.record.service.S3Service;
import org.recordy.server.record.service.dto.FileUrl;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/records")
@RestController
public class RecordController implements RecordApi {

    private final RecordService recordService;
    private final BookmarkService bookmarkService;
    private final S3Service s3Service;

    @GetMapping("/presigned-url")
    public ResponseEntity<FileUrl> getPresignedUrls() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(s3Service.generatePresignedUrl());
    }

    @Override
    @PostMapping
    public ResponseEntity<Void> createRecord(
            @UserId Long uploaderId,
            @RequestBody RecordCreateRequest request) {
        RecordCreate recordCreate = RecordCreate.of(uploaderId, request);
        Record record = recordService.create(recordCreate);

        return ResponseEntity
                .ok()
                .build();
    }

    @Override
    @DeleteMapping("/{recordId}")
    public ResponseEntity<Void> deleteRecord(
            @UserId Long uploaderId,
            @PathVariable Long recordId
    ) {
        recordService.delete(uploaderId, recordId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    @GetMapping("/recent")
    public ResponseEntity<CursorBasePaginatedResponse<RecordInfoWithBookmark>> getRecentRecordInfosWithBookmarks(
            @UserId Long userId,
            @RequestParam(required = false) String keywords,
            @RequestParam(required = false, defaultValue = "0") Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Slice<Record> records = recordService.getRecentRecords(keywords, cursorId, size);
        List<Boolean> bookmarks = bookmarkService.findBookmarks(userId, records.getContent());

        return ResponseEntity
                .ok().
                body(CursorBasePaginatedResponse.of(RecordInfoWithBookmark.of(records, bookmarks, userId),recordInfoWithBookmark -> recordInfoWithBookmark.recordInfo().id()));
    }

    @Override
    @GetMapping("/famous")
    public ResponseEntity<PaginatedResponse<RecordInfoWithBookmark>> getFamousRecordInfoWithBookmarks(
            @UserId Long userId,
            @RequestParam(required = false) String keywords,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ){
        Slice<Record> records = recordService.getFamousRecords(keywords, pageNumber, pageSize);
        List<Boolean> bookmarks = bookmarkService.findBookmarks(userId, records.getContent());

        return ResponseEntity
                .ok()
                .body(PaginatedResponse.of(RecordInfoWithBookmark.of(records, bookmarks, userId)));
    }

    @Override
    @PostMapping("/{recordId}")
    public ResponseEntity<Void> watch(
            @UserId Long userId,
            @PathVariable Long recordId
    ) {
        recordService.watch(userId, recordId);
        return ResponseEntity
                .ok()
                .build();
    }

    @Override
    @GetMapping("/user/{otherUserId}")
    public ResponseEntity<CursorBasePaginatedResponse<RecordInfoWithBookmark>> getRecentRecordInfosWithBookmarksByUser(
            @UserId Long userId,
            @PathVariable Long otherUserId,
            @RequestParam(required = false, defaultValue = "0") long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Slice<Record> records = recordService.getRecentRecordsByUser(otherUserId, cursorId, size);
        List<Boolean> bookmarks = bookmarkService.findBookmarks(otherUserId, records.getContent());

        return ResponseEntity
                .ok()
                .body(CursorBasePaginatedResponse.of(RecordInfoWithBookmark.of(records, bookmarks, userId), recordInfoWithBookmark -> recordInfoWithBookmark.recordInfo()
                        .id()));
    }

    @Override
    @GetMapping("/follow")
    public ResponseEntity<CursorBasePaginatedResponse<RecordInfoWithBookmark>> getSubscribingRecordInfosWithBookmarks(
            @UserId Long userId,
            @RequestParam(required = false, defaultValue = "0") long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Slice<Record> records = recordService.getSubscribingRecords(userId, cursorId, size);
        List<Boolean> bookmarks = bookmarkService.findBookmarks(userId, records.getContent());

        return ResponseEntity
                .ok()
                .body(CursorBasePaginatedResponse.of(RecordInfoWithBookmark.of(records, bookmarks, userId), recordInfoWithBookmark -> recordInfoWithBookmark.recordInfo()
                        .id()));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<RecordInfoWithBookmark>> getTotalRecordInfosWithBookmarks(
            @UserId Long userId,
            @RequestParam(required = false, defaultValue = "10") int size
    ){
        List<Record> records = recordService.getTotalRecords(size);
        List<Boolean> bookmarks = bookmarkService.findBookmarks(userId, records);

        return ResponseEntity
                .ok()
                .body(RecordInfoWithBookmark.of(records, bookmarks, userId));
    }

    @Override
    @GetMapping("/bookmarks")
    public ResponseEntity<CursorBasePaginatedResponse<RecordInfoWithBookmark>> getBookmarkedRecords(
            @UserId Long userId,
            @RequestParam(required = false, defaultValue = "0") long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Slice<Record> records = recordService.getBookmarkedRecords(userId, cursorId, size);
        List<Boolean> bookmarks = bookmarkService.findBookmarks(userId, records.getContent());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CursorBasePaginatedResponse.of(RecordInfoWithBookmark.of(records, bookmarks, userId), recordInfoWithBookmark -> recordInfoWithBookmark.recordInfo()
                        .id()));
    }
}
