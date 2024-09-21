package org.recordy.server.record.service;

import org.recordy.server.record.controller.dto.request.RecordCreateRequest;
import org.recordy.server.record.controller.dto.response.RecordGetResponse;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface RecordService {

    // command
    Long create(RecordCreateRequest request, long uploaderId);
    void delete(long userId, long recordId);

    // query
    Slice<RecordGetResponse> getRecordsByPlaceId(long placeId, long userId, Long cursorId, int size);
    Slice<RecordGetResponse> getRecentRecordsByUser(long otherUserId, long userId, Long cursorId, int size);
    Slice<RecordGetResponse> getBookmarkedRecords(long userId, Long cursorId, int size);
    List<RecordGetResponse> getSubscribingRecords(long userId, int size);
    List<RecordGetResponse> getRecords(long userId, int size);
}
