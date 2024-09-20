package org.recordy.server.record.repository;

import org.recordy.server.record.controller.dto.response.RecordGetResponse;
import org.recordy.server.record.domain.Record;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface RecordRepository {

    // command
    Long save(Record record);
    void deleteById(long recordId);
    void deleteByUserId(long userId);

    // query
    Record findById(long id);
    Slice<RecordGetResponse> findAllByPlaceIdOrderByIdDesc(long placeId, long userId, Long cursor, int size);
    Slice<Record> findAllByUserIdOrderByIdDesc(long userId, Long cursor, Pageable pageable);
    Slice<Record> findAllBySubscribingUserIdOrderByIdDesc(long userId, Long cursor, Pageable pageable);
    long countAllByUserId(long userId);
    Long findMaxId();
    Long count();
}
