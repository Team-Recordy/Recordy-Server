package org.recordy.server.record.service;

import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.usecase.RecordCreateFile;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface RecordService {

    // command
    Record create(RecordCreateFile recordCreateFile);

    // query
    Slice<Record> getFamousRecords(long cursorId, int size);
    Slice<Record> getRecentRecords(long cursorId, int size);
    Slice<Record> getRecentRecordsByKeyword(List<Keyword> keywords, long cursorId, int size);
    Slice<Record> getRecentRecordsByUser(long userId, long cursorId, int size);
}
