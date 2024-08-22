package org.recordy.server.record.repository;

import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.domain.Record;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface RecordRepository {

    // command
    Record save(Record record);
    void deleteById(long recordId);
    void deleteByUserId(long userId);

    // query
    Record findById(long id);
    Slice<Record> findAllOrderByPopularity(Pageable pageable);
    Slice<Record> findAllByKeywordsOrderByPopularity(List<Keyword> keywords, Pageable pageable);
    Slice<Record> findAllByIdAfterOrderByIdDesc(Long cursor, Pageable pageable);
    Slice<Record> findAllByIdAfterAndKeywordsOrderByIdDesc(List<Keyword> keywords, Long cursor, Pageable pageable);
    Slice<Record> findAllByUserIdOrderByIdDesc(long userId, Long cursor, Pageable pageable);
    Slice<Record> findAllBySubscribingUserIdOrderByIdDesc(long userId, Long cursor, Pageable pageable);
    long countAllByUserId(long userId);
    Long findMaxId();
    Long count();
}
