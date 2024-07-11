package org.recordy.server.record.controller;


import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.security.UserId;
import org.recordy.server.record.controller.dto.request.RecordCreateRequest;
import org.recordy.server.record.domain.File;
import org.recordy.server.record.domain.Record;

import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.record.service.RecordService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/records")
@RestController
public class RecordController {

    private final RecordService recordService;

    @PostMapping
    public ResponseEntity<Record> createRecord(
            @UserId Long uploaderId,
            @RequestPart RecordCreateRequest recordCreateRequest,
            @RequestPart File file
    ) {
        RecordCreate recordCreate = RecordCreate.from(uploaderId, recordCreateRequest);
        Record createdRecord = recordService.create(recordCreate, file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdRecord);
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
    public ResponseEntity<Slice<Record>> getRecentRecords(
            @RequestParam(required = false) List<String> keywords,
            @RequestParam(required = false, defaultValue = "0L") long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Slice<Record> records = recordService.getRecentRecords(keywords, cursorId, size);

        return ResponseEntity
                .ok()
                .body(records);
    }

    @GetMapping("/famous")
    public ResponseEntity<Slice<Record>> getFamousRecords(
            @RequestParam(required = false) List<String> keywords,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ){
        return ResponseEntity
                .ok()
                .body(recordService.getFamousRecords(pageNumber, pageSize));
    }

    @PostMapping("/watch")
    public ResponseEntity<Void> watch(
            @UserId long userId,
            @RequestParam long recordId
    ) {
        recordService.watch(userId, recordId);
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/user")
    public ResponseEntity<Slice<Record>> getRecentRecordsByUser(
            @UserId long userId,
            @RequestParam(required = false, defaultValue = "0L") long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Slice<Record> records = recordService.getRecentRecordsByUser(userId, cursorId, size);

        return ResponseEntity
                .ok()
                .body(records);
    }
}
