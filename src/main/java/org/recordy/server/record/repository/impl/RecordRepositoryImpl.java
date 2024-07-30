package org.recordy.server.record.repository.impl;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.recordy.server.keyword.repository.impl.KeywordJpaRepository;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.RecordEntity;
import org.recordy.server.record.repository.RecordRepository;
import org.springframework.data.domain.Pageable;
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
    private final KeywordJpaRepository keywordJpaRepository;

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
    public Optional<Record> findById(long recordId) {
        return recordJpaRepository.findById(recordId)
                .map(RecordEntity::toDomain);
    }

    @Override
    public Slice<Record> findAllOrderByPopularity(Pageable pageable) {
        return recordQueryDslRepository.findAllOrderByPopularity(pageable)
                .map(RecordEntity::toDomain);
    }

    @Override
    public Slice<Record> findAllByKeywordsOrderByPopularity(List<Keyword> keywords, Pageable pageable) {
        List<KeywordEntity> keywordEntities = keywordJpaRepository.findAll().stream()
                .filter(keyword -> keywords.contains(keyword.toDomain()))
                .toList();

        return recordQueryDslRepository.findAllByKeywordsOrderByPopularity(keywordEntities, pageable)
                .map(RecordEntity::toDomain);
    }

    @Override
    public Slice<Record> findAllByIdAfterOrderByIdDesc(long cursor, Pageable pageable) {
        return recordQueryDslRepository.findAllByIdAfterOrderByIdDesc(cursor, pageable)
                .map(RecordEntity::toDomain);
    }

    @Override
    public Slice<Record> findAllByIdAfterAndKeywordsOrderByIdDesc(List<Keyword> keywords, long cursor, Pageable pageable) {
        List<KeywordEntity> keywordEntities = keywordJpaRepository.findAll().stream()
                .filter(keyword -> keywords.contains(keyword.toDomain()))
                .toList();

        return recordQueryDslRepository.findAllByIdAfterAndKeywordsOrderByIdDesc(keywordEntities, cursor, pageable)
                .map(RecordEntity::toDomain);
    }

    @Override
    public Slice<Record> findAllByUserIdOrderByIdDesc(long userId, long cursor, Pageable pageable) {
        return recordQueryDslRepository.findAllByUserIdOrderByIdDesc(userId, cursor, pageable)
                .map(RecordEntity::toDomain);
    }

    @Override
    public Slice<Record> findAllBySubscribingUserIdOrderByIdDesc(long userId, long cursor, Pageable pageable) {
        return recordQueryDslRepository.findAllBySubscribingUserIdOrderByIdDesc(userId, cursor, pageable)
                .map(RecordEntity::toDomain);
    }

    @Override
    public long countAllByUserId(long userId) {
        return recordQueryDslRepository.countAllByUserId(userId);
    }

    @Override
    public Optional<Long> findMaxId() {
        return recordQueryDslRepository.findMaxId();
    }

    @Override
    public Long count() {
        return recordJpaRepository.count();
    }
}
