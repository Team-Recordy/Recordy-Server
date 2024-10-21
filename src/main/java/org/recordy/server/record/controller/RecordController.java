package org.recordy.server.record.controller;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.dto.response.CursorBasePaginatedResponse;
import org.recordy.server.auth.security.resolver.UserId;
import org.recordy.server.record.controller.dto.request.RecordCreateRequest;
import org.recordy.server.record.controller.dto.response.RecordGetResponse;

import org.recordy.server.record.service.RecordService;
import org.recordy.server.record.service.S3Service;
import org.recordy.server.record.domain.FileUrl;
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
    private final S3Service s3Service;

    @GetMapping("/presigned-url")
    public ResponseEntity<FileUrl> getPresignedFileUrl() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(s3Service.generateFilePresignedUrl());
    }

    @Override
    @PostMapping
    public ResponseEntity<Void> create(
            @UserId Long uploaderId,
            @RequestBody RecordCreateRequest request
    ) {
        recordService.create(request, uploaderId);

        return ResponseEntity
                .ok()
                .build();
    }

    @Override
    @DeleteMapping("/{recordId}")
    public ResponseEntity<Void> delete(
            @UserId Long uploaderId,
            @PathVariable Long recordId
    ) {
        recordService.delete(uploaderId, recordId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @GetMapping("/place")
    public ResponseEntity<CursorBasePaginatedResponse<RecordGetResponse>> getAllByPlace(
            @UserId Long userId,
            @RequestParam Long placeId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Slice<RecordGetResponse> records = recordService.getRecordsByPlaceId(placeId, userId, cursorId, size);

        return ResponseEntity
                .ok()
                .body(CursorBasePaginatedResponse.of(records));
    }

    @Override
    @GetMapping("/user/{otherUserId}")
    public ResponseEntity<CursorBasePaginatedResponse<RecordGetResponse>> getAllByUser(
            @UserId Long userId,
            @PathVariable Long otherUserId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Slice<RecordGetResponse> records = recordService.getRecentRecordsByUser(otherUserId, userId, cursorId, size);

        return ResponseEntity
                .ok()
                .body(CursorBasePaginatedResponse.of(records));
    }

    @Override
    @GetMapping("/follow")
    public ResponseEntity<List<RecordGetResponse>> getRandomBySubscription(
            @UserId Long userId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return ResponseEntity
                .ok()
                .body(recordService.getSubscribingRecords(userId, size));
    }

    @Override
    @GetMapping("/random")
    public ResponseEntity<List<RecordGetResponse>> getRandom(
            @UserId Long userId,
            @RequestParam(required = false, defaultValue = "10") int size
    ){
        return ResponseEntity
                .ok()
                .body(recordService.getRecords(userId, size));
    }

    @Override
    @GetMapping("/bookmarks")
    public ResponseEntity<CursorBasePaginatedResponse<RecordGetResponse>> getAllBookmarked(
            @UserId Long userId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Slice<RecordGetResponse> records = recordService.getBookmarkedRecords(userId, cursorId, size);

        return ResponseEntity
                .ok()
                .body(CursorBasePaginatedResponse.of(records));
    }
}
