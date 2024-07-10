package org.recordy.server.record.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.exception.RecordyException;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.domain.File;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.record.exception.RecordException;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.record.service.FileService;
import org.recordy.server.record.service.RecordService;
import org.recordy.server.record.controller.dto.FileUrl;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.user.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final FileService fileService;
    private final UserService userService;

    @Override
    public Record create(RecordCreate recordCreate, File file) {
        FileUrl fileUrl = fileService.save(file);
        User user = userService.getById(recordCreate.uploaderId())
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND));

        return recordRepository.save(Record.builder()
                .fileUrl(fileUrl)
                .location(recordCreate.location())
                .content(recordCreate.content())
                .uploader(user)
                .build());
    }

    @Override
    public void delete(long userId, long recordId) {
        Record record = recordRepository.findById(recordId)
                        .orElseThrow(() -> new RecordException(ErrorMessage.RECORD_NOT_FOUND));
        if (!record.isUploader(recordId)) {
            throw new RecordyException(ErrorMessage.FORBIDDEN_DELETE_RECORD);
        }
        recordRepository.deleteById(recordId);
    }

    @Override
    public Slice<Record> getFamousRecords(long cursorId, int size) {
        return null;
    }

    @Override
    public Slice<Record> getRecentRecordsLaterThanCursor(long cursorId, int size) {
        return recordRepository.findAllByIdAfterOrderByIdDesc(cursorId, PageRequest.ofSize(size));
    }

    @Override
    public Slice<Record> getRecentRecordsByKeywords(List<Keyword> keywords, long cursorId, int size) {
        return recordRepository.findAllByIdAfterAndKeywordsOrderByIdDesc(keywords, cursorId, PageRequest.ofSize(size));
    }

    @Override
    public Slice<Record> getRecentRecordsByUser(long userId, long cursorId, int size) {
        return recordRepository.findAllByUserIdOrderByIdDesc(userId, cursorId, PageRequest.ofSize(size));
    }

    public Slice<Record> getRecentRecords(Long cursorId, int size, List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return getRecentRecordsLaterThanCursor(cursorId, size);
        } else {
            List<Keyword> keywordEnums = keywords.stream()
                    .map(Keyword::valueOf)
                    .collect(Collectors.toList());
            return getRecentRecordsByKeywords(keywordEnums, cursorId, size);
        }
    }
}
