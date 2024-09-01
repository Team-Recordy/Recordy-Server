package org.recordy.server.record.service;

import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.usecase.RecordCreate;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface RecordService {

    // command
    Record create(RecordCreate recordCreate);
    void delete(long userId, long recordId);

    // query
    void watch(long userId, long recordId);
    Slice<Record> getFamousRecords(int pageNumber, int size);
    Slice<Record> getRecentRecords(Long cursorId, int size);
    Slice<Record> getRecentRecordsByUser(long userId, Long cursorId, int size);
    Slice<Record> getSubscribingRecords(long userId, Long cursorId, int size);
    List<Record> getTotalRecords(int size);
}
