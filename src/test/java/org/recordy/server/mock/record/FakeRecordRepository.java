package org.recordy.server.mock.record;

import org.recordy.server.record.controller.dto.response.RecordGetResponse;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.repository.RecordRepository;
import org.springframework.data.domain.Slice;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FakeRecordRepository implements RecordRepository {

    public long recordAutoIncrementId = 1L;
    private final Map<Long, Record> records = new ConcurrentHashMap<>();

    @Override
    public Long save(Record record) {
        Record realRecord = Record.builder()
                .id(recordAutoIncrementId)
                .fileUrl(record.getFileUrl())
                .content(record.getContent())
                .uploader(record.getUploader())
                .build();

        records.put(recordAutoIncrementId++, realRecord);

        return realRecord.getId();
    }

    @Override
    public void deleteById(long recordId) {
        records.remove(recordId);
    }

    @Override
    public void deleteByUserId(long userId) {
        records.values().stream()
                .filter(record -> record.getUploader().getId().equals(userId))
                .forEach(record -> records.remove(record.getId()));
    }

    @Override
    public Record findById(long recordId) {
        return records.get(recordId);
    }

    @Override
    public Slice<RecordGetResponse> findAllByPlaceIdOrderByIdDesc(long placeId, long userId, Long cursor, int size) {
        return null;
    }

    @Override
    public Slice<RecordGetResponse> findAllByUserIdOrderByIdDesc(long otherUserId, long userId, Long cursor, int size) {
        return null;
    }

    @Override
    public Slice<RecordGetResponse> findAllByBookmarkOrderByIdDesc(long userId, Long cursor, int size) {
        return null;
    }

    @Override
    public List<Long> findAllIdsBySubscribingUserId(long userId) {
        return List.of();
    }

    @Override
    public List<Long> findAllIds() {
        return List.of();
    }

    @Override
    public List<RecordGetResponse> findAllByIds(List<Long> ids, long userId) {
        return List.of();
    }
}
