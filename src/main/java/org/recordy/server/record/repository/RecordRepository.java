package org.recordy.server.record.repository;

import java.util.Optional;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.domain.Record;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface RecordRepository {

    // command
    Record save(Record record);
    void deleteById(long recordId);

    // query
    Optional<Record> findById(long recordId);
    Slice<Record> findAllOrderByPopularity(long cursor, Pageable pageable);
    Slice<Record> findAllByIdAfterOrderByIdDesc(long cursor, Pageable pageable);
    Slice<Record> findAllByIdAfterAndKeywordsOrderByIdDesc(List<Keyword> keywords, long cursor, Pageable pageable);
    Slice<Record> findAllByUserIdOrderByIdDesc(long userId, long cursor, Pageable pageable);
}
