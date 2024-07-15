package org.recordy.server.record.controller;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.security.UserId;
import org.recordy.server.record.controller.dto.request.RecordCreateRequest;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/v1/records")
@RestController
public class RecordController implements RecordApi {

    private final RecordService recordService;
    private final S3Service s3Service;

    @GetMapping("/presigned-url")
    public ResponseEntity<FileUrl> getPresignedUrls() {
        String videoPresignedUrl = s3Service.generatePresignedUrl("videos/");
        String thumbnailPresignedUrl = s3Service.generatePresignedUrl("thumbnails/");

        FileUrl fileUrl = new FileUrl(videoPresignedUrl, thumbnailPresignedUrl);
        return ResponseEntity.ok(fileUrl);
    }

    @Override
    @PostMapping
    public ResponseEntity<Record> createRecord(
            @UserId Long uploaderId,
            @RequestBody RecordCreateRequest request) {

        RecordCreate recordCreate = RecordCreate.from(uploaderId, request);
        Record record = recordService.create(recordCreate, new File(request.fileUrl().videoUrl(), request.fileUrl().thumbnailUrl()));

        return ResponseEntity.status(HttpStatus.CREATED).body(record);
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
            @RequestParam(required = false) List<String> keywords,
            @RequestParam(required = false, defaultValue = "0") Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Slice<Record> records = recordService.getRecentRecords(keywords, cursorId, size);

        return ResponseEntity
                .ok()
                .body(records);
    }

    @Override
    @GetMapping("/famous")
    public ResponseEntity<Slice<Record>> getFamousRecords(
            @RequestParam(required = false) List<String> keywords,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        return ResponseEntity
                .ok()
                .body(recordService.getFamousRecords(keywords, pageNumber, pageSize));
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
            @UserId Long userId,
            @RequestParam(required = false, defaultValue = "0") Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Slice<Record> records = recordService.getRecentRecordsByUser(userId, cursorId, size);

        return ResponseEntity
                .ok()
                .body(records);
    }
}

