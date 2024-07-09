package org.recordy.server.record.controller;


import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.security.UserId;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.controller.dto.RecordCreateRequest;
import org.recordy.server.record.domain.File;
import org.recordy.server.record.domain.Record;

import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.record.service.RecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/api/v1/records")
@RestController
public class RecordController{

    private final RecordService recordService;

    @PostMapping
    public ResponseEntity<Record> createRecord(
            @UserId Long userId,
            @RequestPart RecordCreateRequest recordCreateRequest,
            @RequestPart File file){

        List<Keyword> keywords = recordCreateRequest.keywords().stream()
                .map(Keyword::fromString)
                .collect(Collectors.toList());

        RecordCreate recordCreate = RecordCreate.of(userId, recordCreateRequest.location(), recordCreateRequest.content(), keywords);
        Record createdRecord = recordService.create(recordCreate, file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdRecord);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteRecord(
            @UserId Long userId,
            @PathVariable Long recordId
    ){
        recordService.delete(userId, recordId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
