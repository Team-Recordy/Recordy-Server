package org.recordy.server.record.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.domain.File;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.record.service.FileService;
import org.recordy.server.record.service.RecordService;
import org.recordy.server.record.service.dto.FileUrl;
import org.recordy.server.user.service.UserService;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final FileService fileService;
    private final UserService userService;

    @Override
    public Record create(RecordCreate recordCreate, File file) {
        FileUrl fileUrl = fileService.save(file);


        return null;
    }

    @Override
    public Slice<Record> getFamousRecords(long cursorId, int size) {
        return null;
    }

    @Override
    public Slice<Record> getRecentRecords(long cursorId, int size) {
        return null;
    }

    @Override
    public Slice<Record> getRecentRecordsByKeyword(List<Keyword> keywords, long cursorId, int size) {
        return null;
    }

    @Override
    public Slice<Record> getRecentRecordsByUser(long userId, long cursorId, int size) {
        return null;
    }
}
