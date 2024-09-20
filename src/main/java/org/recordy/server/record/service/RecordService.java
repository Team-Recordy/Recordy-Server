package org.recordy.server.record.service;

import org.recordy.server.record.controller.dto.request.RecordCreateRequest;
import org.recordy.server.record.controller.dto.response.RecordGetResponse;
import org.recordy.server.record.domain.Record;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface RecordService {

    // command
    Long create(RecordCreateRequest request, long uploaderId);
    void delete(long userId, long recordId);

    // query
    Slice<RecordGetResponse> getRecordsByPlaceId(long placeId, long userId, Long cursorId, int size);
    Slice<Record> getFamousRecords(int pageNumber, int size);
    Slice<Record> getRecentRecords(Long cursorId, int size);
    Slice<Record> getRecentRecordsByUser(long userId, Long cursorId, int size);
    Slice<Record> getSubscribingRecords(long userId, Long cursorId, int size);
    List<Record> getTotalRecords(int size);
}
