package org.recordy.server.record.repository;

import org.recordy.server.record.controller.dto.response.RecordGetResponse;
import org.recordy.server.record.domain.Record;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface RecordRepository {

    // command
    Long save(Record record);
    void deleteById(long recordId);
    void deleteByUserId(long userId);

    // query
    Record findById(long id);
    Slice<RecordGetResponse> findAllByPlaceIdOrderByIdDesc(long placeId, long userId, Long cursor, int size);
    Slice<RecordGetResponse> findAllByUserIdOrderByIdDesc(long otherUserId, long userId, Long cursor, int size);
    Slice<RecordGetResponse> findAllByBookmarkOrderByIdDesc(long userId, Long cursor, int size);
    List<RecordGetResponse> findAllByIds(List<Long> ids, long userId);
    List<Long> findAllIdsBySubscribingUserId(long userId);
    List<Long> findAllIds();
}
