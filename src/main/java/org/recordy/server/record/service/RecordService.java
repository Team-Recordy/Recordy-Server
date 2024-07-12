package org.recordy.server.record.service;

import org.recordy.server.record.domain.File;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.usecase.RecordCreate;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface RecordService {

    // command
    Record create(RecordCreate recordCreate, File file);
    void delete(long userId, long recordId);

    // query
    void watch(long userId, long recordId);
    Slice<Record> getFamousRecords(List<String> keywords, int pageNumber, int size);
    Slice<Record> getRecentRecords(List<String> keywords, Long cursorId, int size);
    Slice<Record> getRecentRecordsByUser(long userId, long cursorId, int size);
    Slice<Record> getSubscribingRecords(long userId, long cursorId, int size);
}
