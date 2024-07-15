package org.recordy.server.record.controller;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.security.UserId;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.controller.dto.request.RecordCreateRequest;
import org.recordy.server.record.domain.File;
import org.recordy.server.record.domain.Record;

import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.record.service.RecordService;
import org.recordy.server.record.service.S3Service;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/v1/records")
@RestController
public class RecordController implements RecordApi {

    private final RecordService recordService;
    private final S3Service s3Service;

    @Override
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
    public ResponseEntity<Slice<Record>> getRecentRecords(
            @RequestParam(required = false) byte[] keywords,
            @RequestParam(required = false, defaultValue = "0") Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Slice<Record> records = recordService.getRecentRecords(Keyword.decode(keywords), cursorId, size);

        return ResponseEntity
                .ok()
                .body(records);
    }

    @Override
    @GetMapping("/famous")
    public ResponseEntity<Slice<Record>> getFamousRecords(
            @RequestParam(required = false) byte[] keywords,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        return ResponseEntity
                .ok()
                .body(recordService.getFamousRecords(Keyword.decode(keywords), pageNumber, pageSize));
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
    @GetMapping
    public ResponseEntity<Slice<Record>> getRecentRecordsByUser(
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "0") Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Slice<Record> records = recordService.getRecentRecordsByUser(userId, cursorId, size);

        return ResponseEntity
                .ok()
                .body(records);
    }
}
