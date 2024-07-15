package org.recordy.server.record_stat.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.security.resolver.UserId;
import org.recordy.server.record.controller.dto.response.RecordInfoWithBookmark;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record_stat.domain.usecase.Preference;
import org.recordy.server.record_stat.service.RecordStatService;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/records")
@RestController
public class RecordStatController implements RecordStatApi{
    private final RecordStatService recordStatService;

    @Override
    @PostMapping("/bookmark/{recordId}")
    public ResponseEntity<Void> bookmark(
            @UserId Long userId,
            @PathVariable Long recordId
    ) {
        recordStatService.bookmark(userId, recordId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @DeleteMapping("/bookmark/{recordId}")
    public ResponseEntity<Void> deleteBookmark(
            @UserId Long userId,
            @PathVariable Long recordId
    ) {
        recordStatService.deleteBookmark(userId, recordId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Override
    @GetMapping("/preference")
    public ResponseEntity<Preference> getPreference(
            @UserId  Long userId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(recordStatService.getPreference(userId));
    }

    @Override
    @GetMapping("/bookmark")
    public ResponseEntity<Slice<RecordInfoWithBookmark>> getBookmarkedRecords(
            @UserId Long userId,
            @RequestParam(required = false, defaultValue = "0") long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Slice<Record> records = recordStatService.getBookmarkedRecords(userId, cursorId, size);
        List<Boolean> bookmarks = recordStatService.findBookmarks(userId, records.getContent());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(RecordInfoWithBookmark.of(records, bookmarks));
    }
}
