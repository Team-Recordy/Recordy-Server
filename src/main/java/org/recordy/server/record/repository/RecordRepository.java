package org.recordy.server.record.repository;

import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.domain.Record;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RecordRepository {

    // command
    Record save(Record record);
    void deleteById(long recordId);

    // query
    Optional<Record> findById(long id);
    Slice<Record> findAllOrderByPopularity(Pageable pageable);
    Slice<Record> findAllByKeywordsOrderByPopularity(List<Keyword> keywords, Pageable pageable);
    Slice<Record> findAllByIdAfterOrderByIdDesc(long cursor, Pageable pageable);
    Slice<Record> findAllByIdAfterAndKeywordsOrderByIdDesc(List<Keyword> keywords, long cursor, Pageable pageable);
    Slice<Record> findAllByUserIdOrderByIdDesc(long userId, long cursor, Pageable pageable);
    Map<Keyword, Long> countAllByUserIdGroupByKeyword(long userId);
    Slice<Record> findAllBySubscribingUserIdOrderByIdDesc(long userId, long cursor, Pageable pageable);
    Optional<Long> findMaxId();
    Long count();
}
