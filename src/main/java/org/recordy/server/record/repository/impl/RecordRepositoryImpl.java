package org.recordy.server.record.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.RecordEntity;
import org.recordy.server.record.exception.RecordException;
import org.recordy.server.record.repository.RecordRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class RecordRepositoryImpl implements RecordRepository {

    private final RecordJpaRepository recordJpaRepository;
    private final RecordQueryDslRepository recordQueryDslRepository;

    @Transactional
    @Override
    public Record save(Record record) {
        return recordJpaRepository.save(RecordEntity.from(record))
                .toDomain();
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
                .map(RecordEntity::toDomain)
                .orElseThrow(() -> new RecordException(ErrorMessage.RECORD_NOT_FOUND));
    }

    @Override
    public Slice<Record> findAllOrderByPopularity(Pageable pageable) {
        return recordQueryDslRepository.findAllOrderByPopularity(pageable)
                .map(RecordEntity::toDomain);
    }

    @Override
    public Slice<Record> findAllByIdAfterOrderByIdDesc(Long cursor, Pageable pageable) {
        return recordQueryDslRepository.findAllByIdAfterOrderByIdDesc(cursor, pageable)
                .map(RecordEntity::toDomain);
    }

    @Override
    public Slice<Record> findAllByUserIdOrderByIdDesc(long userId, Long cursor, Pageable pageable) {
        return recordQueryDslRepository.findAllByUserIdOrderByIdDesc(userId, cursor, pageable)
                .map(RecordEntity::toDomain);
    }

    @Override
    public Slice<Record> findAllBySubscribingUserIdOrderByIdDesc(long userId, Long cursor, Pageable pageable) {
        return recordQueryDslRepository.findAllBySubscribingUserIdOrderByIdDesc(userId, cursor, pageable)
                .map(RecordEntity::toDomain);
    }

    @Override
    public long countAllByUserId(long userId) {
        return recordQueryDslRepository.countAllByUserId(userId);
    }

    @Override
    public Long findMaxId() {
        return recordQueryDslRepository.findMaxId()
                .orElse(0L);
    }

    @Override
    public Long count() {
        return recordJpaRepository.count();
    }
}
