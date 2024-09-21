package org.recordy.server.record.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.record.controller.dto.response.RecordGetResponse;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.RecordEntity;
import org.recordy.server.record.exception.RecordException;
import org.recordy.server.record.repository.RecordRepository;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class RecordRepositoryImpl implements RecordRepository {

    private final RecordJpaRepository recordJpaRepository;
    private final RecordQueryDslRepository recordQueryDslRepository;

    @Transactional
    @Override
    public Long save(Record record) {
        return recordJpaRepository.save(RecordEntity.from(record))
                .getId();
    }

    @Transactional
    @Override
    public void deleteById(long recordId) {
        recordJpaRepository.findById(recordId)
                .ifPresent(recordJpaRepository::delete);
    }

    @Transactional
    @Override
    public void deleteByUserId(long userId) {
        recordJpaRepository.deleteAllByUserId(userId);
    }

    @Override
    public Record findById(long recordId) {
        return recordJpaRepository.findById(recordId)
                .map(Record::from)
                .orElseThrow(() -> new RecordException(ErrorMessage.RECORD_NOT_FOUND));
    }

    @Override
    public Slice<RecordGetResponse> findAllByPlaceIdOrderByIdDesc(long placeId, long userId, Long cursor, int size) {
        return recordQueryDslRepository.findAllByPlaceIdOrderByIdDesc(placeId, userId, cursor, size);
    }

    @Override
    public Slice<RecordGetResponse> findAllByUserIdOrderByIdDesc(long otherUserId, long userId, Long cursor, int size) {
        return recordQueryDslRepository.findAllByUserIdOrderByIdDesc(otherUserId, userId, cursor, size);
    }

    @Override
    public Slice<RecordGetResponse> findAllByBookmarkOrderByIdDesc(long userId, Long cursor, int size) {
        return recordQueryDslRepository.findAllByBookmarkOrderByIdDesc(userId, cursor, size);
    }

    @Override
    public List<Long> findAllIdsBySubscribingUserId(long userId) {
        return recordQueryDslRepository.findAllIdsBySubscribingUserId(userId);
    }

    @Override
    public List<Long> findAllIds() {
        return recordQueryDslRepository.findAllIds();
    }

    @Override
    public List<RecordGetResponse> findAllByIds(List<Long> ids, long userId) {
        return recordQueryDslRepository.findAllByIds(ids, userId);
    }
}
