package org.recordy.server.record.controller;


import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.security.UserId;
import org.recordy.server.record.controller.dto.request.RecordCreateRequest;
import org.recordy.server.record.controller.dto.response.RecordInfo;
import org.recordy.server.record.controller.dto.response.RecordInfoWithBookmark;
import org.recordy.server.record.domain.File;
import org.recordy.server.record.domain.Record;

import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.record.service.RecordService;
import org.recordy.server.record_stat.service.RecordStatService;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/records")
@RestController
public class RecordController {

    private final RecordService recordService;

    @PostMapping
    public ResponseEntity<Record> createRecord(
            @UserId Long uploaderId,
            @RequestPart RecordCreateRequest request,
            @RequestPart MultipartFile thumbnail,
            @RequestPart MultipartFile video
    ) {
        RecordCreate recordCreate = RecordCreate.from(uploaderId, request);
        Record record = recordService.create(recordCreate, File.of(video, thumbnail));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(record);
    }

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

    @GetMapping("/recent")
    public ResponseEntity<Slice<RecordInfoWithBookmark>> getRecentRecordInfoWithBookmarks(
            @UserId Long userId,
            @RequestParam(required = false) List<String> keywords,
            @RequestParam(required = false, defaultValue = "0") long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {

        Slice<RecordInfoWithBookmark> recordInfoWithBookmarks = recordService.getRecentRecordInfoWithBookmarks(userId, keywords, cursorId, size);
        return ResponseEntity.ok().body(recordInfoWithBookmarks);
    }

    @GetMapping("/famous")
    public ResponseEntity<Slice<RecordInfoWithBookmark>> getFamousRecordInfoWithBookmarks(
            @UserId Long userId,
            @RequestParam(required = false) List<String> keywords,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ){
        return ResponseEntity
                .ok()
                .body(recordService.getFamousRecordInfoWithBookmarks(userId, keywords, pageNumber, pageSize));
    }

    @PostMapping("/{recordId}")
    public ResponseEntity<Void> watch(
            @UserId Long userId,
            @PathVariable long recordId
    ) {
        recordService.watch(userId, recordId);
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/user")
    public ResponseEntity<Slice<RecordInfoWithBookmark>> getRecentRecordInfoWithBookmarksByUser(
            @UserId long userId,
            @RequestParam(required = false, defaultValue = "0") long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Slice<RecordInfoWithBookmark> records = recordService.getRecentRecordInfoWithBookmarksByUser(userId, cursorId, size);

        return ResponseEntity
                .ok()
                .body(records);
    }
}
