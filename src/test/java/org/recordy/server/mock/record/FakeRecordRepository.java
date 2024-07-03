package org.recordy.server.mock.record;

import org.recordy.server.record.domain.Record;
import org.recordy.server.record.repository.RecordRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeRecordRepository implements RecordRepository {

    public long autoIncrementId = 1L;
    private final Map<Long, Record> records = new HashMap<>();

    @Override
    public Record save(Record record) {
        Record realRecord = Record.builder()
                .id(autoIncrementId)
                .fileUrl(record.getFileUrl())
                .location(record.getLocation())
                .content(record.getContent())
                .uploader(record.getUploader())
                .build();

        records.put(autoIncrementId++, realRecord);

        return realRecord;
    }

    @Override
    public Slice<Record> findAllOrderByPopularity(long cursor, Pageable pageable) {
        return null;
    }

    @Override
    public Slice<Record> findAllOrderByCreatedAtDesc(long cursor, Pageable pageable) {
        return null;
    }

    @Override
    public Slice<Record> findAllByKeywordIdsOrderByCreatedAtDesc(List<Long> keywordIds, long cursor, Pageable pageable) {
        return null;
    }

    @Override
    public Slice<Record> findAllByUserIdOrderByCreatedAtDesc(long userId, long cursor, Pageable pageable) {
        return null;
    }
}
