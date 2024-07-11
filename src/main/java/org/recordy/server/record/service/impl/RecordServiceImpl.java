package org.recordy.server.record.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.domain.File;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.record.exception.RecordException;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.record.service.FileService;
import org.recordy.server.record.service.RecordService;
import org.recordy.server.record.service.dto.FileUrl;
import org.recordy.server.record_stat.domain.View;
import org.recordy.server.record_stat.repository.ViewRepository;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.user.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final ViewRepository viewRepository;
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
                .keywords(recordCreate.keywords())
                .build());
    }

    @Override
    public void delete(long userId, long recordId) {
        Record record = recordRepository.findById(recordId)
                        .orElseThrow(() -> new RecordException(ErrorMessage.RECORD_NOT_FOUND));
        if (!record.isUploader(userId)) {
            throw new RecordException(ErrorMessage.FORBIDDEN_DELETE_RECORD);
        }
        recordRepository.deleteById(recordId);
    }

    @Override
    public void watch(long userId, long recordId) {
        User user = userService.getById(userId)
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND));
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new RecordException(ErrorMessage.RECORD_NOT_FOUND));
        viewRepository.save(View.builder()
                .record(record)
                .user(user)
                .build());
    }

    @Override
    public Slice<Record> getFamousRecords(List<String> keywords, int pageNumber, int size) {
        if (Objects.isNull(keywords) || keywords.isEmpty()) {
            return getFamousRecords(pageNumber, size);
        }

        return getFamousRecordsWithKeywords(Keyword.from(keywords), pageNumber, size);
    }

    private Slice<Record> getFamousRecords(int pageNumber, int size) {
        return recordRepository.findAllOrderByPopularity(PageRequest.of(pageNumber, size));
    }

    private Slice<Record> getFamousRecordsWithKeywords(List<Keyword> keywords, int pageNumber, int size) {
        return recordRepository.findAllByKeywordsOrderByPopularity(keywords, PageRequest.of(pageNumber, size));
    }

    @Override
    public Slice<Record> getRecentRecordsByUser(long userId, long cursorId, int size) {
        return recordRepository.findAllByUserIdOrderByIdDesc(userId, cursorId, PageRequest.ofSize(size));
    }

    @Override
    public Slice<Record> getRecentRecords(List<String> keywords, Long cursorId, int size) {
        if (Objects.isNull(keywords) || keywords.isEmpty()) {
            return getRecentRecords(cursorId, size);
        }

        return getRecentRecordsWithKeywords(Keyword.from(keywords), cursorId, size);
    }

    private Slice<Record> getRecentRecords(long cursorId, int size) {
        return recordRepository.findAllByIdAfterOrderByIdDesc(cursorId, PageRequest.ofSize(size));
    }

    private Slice<Record> getRecentRecordsWithKeywords(List<Keyword> keywords, long cursorId, int size) {
        return recordRepository.findAllByIdAfterAndKeywordsOrderByIdDesc(keywords, cursorId, PageRequest.ofSize(size));
    }
}
