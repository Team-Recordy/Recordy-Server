package org.recordy.server.record.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.record.exception.RecordException;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.record.service.RecordService;
import org.recordy.server.record.service.S3Service;
import org.recordy.server.record.service.dto.FileUrl;
import org.recordy.server.view.domain.View;
import org.recordy.server.view.repository.ViewRepository;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RecordServiceImpl implements RecordService {

    private final S3Service s3Service;
    private final RecordRepository recordRepository;
    private final ViewRepository viewRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Record create(RecordCreate recordCreate) {
        User user = userRepository.findById(recordCreate.uploaderId())
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND));

        FileUrl fileUrl = s3Service.convertToCloudFrontUrl(recordCreate.fileUrl());

        return recordRepository.save(Record.builder()
                .fileUrl(fileUrl)
                .location(recordCreate.location())
                .content(recordCreate.content())
                .uploader(user)
                .keywords(recordCreate.keywords())
                .build());
    }

    @Transactional
    @Override
    public void delete(long userId, long recordId) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new RecordException(ErrorMessage.RECORD_NOT_FOUND));
        if (!record.isUploader(userId)) {
            throw new RecordException(ErrorMessage.FORBIDDEN_DELETE_RECORD);
        }
        recordRepository.deleteById(recordId);
    }

    @Transactional
    @Override
    public void watch(long userId, long recordId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND));
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new RecordException(ErrorMessage.RECORD_NOT_FOUND));
        viewRepository.save(View.builder()
                .record(record)
                .user(user)
                .build());
    }

    @Override
    public Slice<Record> getFamousRecords(String keywords, int pageNumber, int size) {
        if (Objects.isNull(keywords) || keywords.isEmpty()) {
            return getFamousRecords(pageNumber, size);
        }

        return getFamousRecordsWithKeywords(Keyword.decode(keywords), pageNumber, size);
    }

    private Slice<Record> getFamousRecords(int pageNumber, int size) {
        return recordRepository.findAllOrderByPopularity(PageRequest.of(pageNumber, size));
    }

    private Slice<Record> getFamousRecordsWithKeywords(List<Keyword> keywords, int pageNumber, int size) {
        return recordRepository.findAllByKeywordsOrderByPopularity(keywords, PageRequest.of(pageNumber, size));
    }

    @Override
    public Slice<Record> getRecentRecordsByUser(long userId, Long cursorId, int size) {
        return recordRepository.findAllByUserIdOrderByIdDesc(userId, cursorId, PageRequest.ofSize(size));
    }

    @Override
    public Slice<Record> getRecentRecords(String keywords, Long cursorId, int size) {
        if (Objects.isNull(keywords)) {
            return getRecentRecords(cursorId, size);
        }

        return getRecentRecordsWithKeywords(Keyword.decode(keywords), cursorId, size);
    }

    private Slice<Record> getRecentRecords(Long cursorId, int size) {
        return recordRepository.findAllByIdAfterOrderByIdDesc(cursorId, PageRequest.ofSize(size));
    }

    private Slice<Record> getRecentRecordsWithKeywords(List<Keyword> keywords, Long cursorId, int size) {
        return recordRepository.findAllByIdAfterAndKeywordsOrderByIdDesc(keywords, cursorId, PageRequest.ofSize(size));
    }

    @Override
    public Slice<Record> getSubscribingRecords(long userId, Long cursorId, int size) {
        return recordRepository.findAllBySubscribingUserIdOrderByIdDesc(userId, cursorId, PageRequest.ofSize(size));
    }

    @Override
    public List<Record> getTotalRecords(int size) {
        Optional<Long> maxId = recordRepository.findMaxId();
        Long count = recordRepository.count();

        Set<Long> selectedIds = new HashSet<>();
        Random random = new Random();
        List<Record> records = new ArrayList<>();

        while (records.size() < size && records.size() < count) {
            long randomId = random.nextLong(maxId.get()) + 1;

            if (!selectedIds.contains(randomId)) {
                selectedIds.add(randomId);

                Optional<Record> findRecord = recordRepository.findById(randomId);
                findRecord.ifPresent(records::add);
            }
        }

        return records;
    }
}
