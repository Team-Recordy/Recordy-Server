package org.recordy.server.mock.record;

import org.recordy.server.record.controller.dto.response.RecordGetResponse;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.repository.RecordRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

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
    public Slice<Record> findAllByUserIdOrderByIdDesc(long userId, Long cursor, Pageable pageable) {
        List<Record> content = records.values().stream()
                .filter(record -> record.getId() < checkCursor(cursor) && record.getUploader().getId() == userId)
                .sorted(Comparator.comparing(Record::getId).reversed())
                .toList();

        if (content.size() < pageable.getPageSize())
            return new SliceImpl<>(content, pageable, false);

        return new SliceImpl<>(content.subList(0, pageable.getPageSize()), pageable, true);
    }

    @Override
    public List<Long> findAllIdsBySubscribingUserId(long userId) {
        return List.of();
    }

    @Override
    public List<RecordGetResponse> findAllByIds(List<Long> ids, long userId) {
        return List.of();
    }

    @Override
    public long countAllByUserId(long userId) {
        return records.values().stream()
                .map(Record::getUploader)
                .filter(user -> user.getId() == userId)
                .count();
    }

    @Override
    public Long findMaxId() {
        return records.keySet().stream()
                .max(Long::compareTo)
                .orElse(0L);
    }

    @Override
    public Long count() {
        return (long) records.size();
    }

    private Long checkCursor(Long cursor){
        if (cursor != null) {
            return cursor;
        }
        return Long.MAX_VALUE;
    }
}
