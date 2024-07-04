package org.recordy.server.record.repository;

import org.recordy.server.record.domain.Record;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface RecordRepository {

    // command
    Record save(Record record);

    // query
    Slice<Record> findAllOrderByPopularity(long cursor, Pageable pageable);
    Slice<Record> findAllByIdAfterOrderByIdDesc(long cursor, Pageable pageable);
    Slice<Record> findAllByIdAfterAndKeywordIdsOrderByIdDesc(List<Long> keywordIds, long cursor, Pageable pageable);
    Slice<Record> findAllByUserIdOrderByCreatedAtDesc(long userId, long cursor, Pageable pageable);
}
