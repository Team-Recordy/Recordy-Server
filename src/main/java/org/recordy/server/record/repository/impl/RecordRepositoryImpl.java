package org.recordy.server.record.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.RecordEntity;
import org.recordy.server.record.repository.RecordRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class RecordRepositoryImpl implements RecordRepository {

    private final RecordJpaRepository recordJpaRepository;
    private final RecordQueryDslRepository recordQueryDslRepository;

    @Override
    public Record save(Record record) {
        return recordJpaRepository.save(RecordEntity.from(record))
                .toDomain();
    }

    @Override
    public Slice<Record> findAllOrderByPopularity(long cursor, Pageable pageable) {
        return null;
    }

    @Override
    public Slice<Record> findAllByIdAfterOrderByIdDesc(long cursor, Pageable pageable) {
        return recordQueryDslRepository.findAllByIdAfterOrderByIdDesc(cursor, pageable)
                .map(RecordEntity::toDomain);
    }

    @Override
    public Slice<Record> findAllByIdAfterAndKeywordIdsOrderByIdDesc(List<Long> keywordIds, long cursor, Pageable pageable) {
        return null;
    }

    @Override
    public Slice<Record> findAllByUserIdOrderByCreatedAtDesc(long userId, long cursor, Pageable pageable) {
        return null;
    }
}
