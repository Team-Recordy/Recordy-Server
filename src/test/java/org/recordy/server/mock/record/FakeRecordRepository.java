package org.recordy.server.mock.record;

import java.util.Optional;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.RecordEntity;
import org.recordy.server.record.domain.UploadEntity;
import org.recordy.server.record.repository.RecordRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.*;

public class FakeRecordRepository implements RecordRepository {

    public long recordAutoIncrementId = 1L;
    private final Map<Long, Record> records = new HashMap<>();

    public long uploadAutoIncrementId = 1L;
    private final Map<Long, UploadEntity> uploads = new HashMap<>();

    @Override
    public Record save(Record record) {
        Record realRecord = Record.builder()
                .id(recordAutoIncrementId)
                .fileUrl(record.getFileUrl())
                .location(record.getLocation())
                .content(record.getContent())
                .keywords(record.getKeywords())
                .uploader(record.getUploader())
                .build();

        records.put(recordAutoIncrementId++, realRecord);

        record.getKeywords().stream()
                .map(keyword -> UploadEntity.builder()
                        .id(uploadAutoIncrementId++)
                        .record(RecordEntity.from(record))
                        .keyword(KeywordEntity.from(keyword))
                        .build())
                .forEach(upload -> uploads.put(upload.getId(), upload));

        return realRecord;
    }

    @Override
    public void deleteById(long recordId) {
        records.remove(recordId);
    }

    @Override
    public Optional<Record> findById(long recordId) {
        return Optional.ofNullable(records.get(recordId));
    }

    @Override
    public Slice<Record> findAllOrderByPopularity(Pageable pageable) {
        return null;
    }

    @Override
    public Slice<Record> findAllByKeywordsOrderByPopularity(List<Keyword> keywords, Pageable pageable) {
        return null;
    }

    @Override
    public Slice<Record> findAllByIdAfterOrderByIdDesc(long cursor, Pageable pageable) {
        List<Record> content = records.keySet().stream()
                .filter(key -> key < cursor)
                .map(records::get)
                .sorted(Comparator.comparing(Record::getId).reversed())
                .toList();

        if (content.size() < pageable.getPageSize())
            return new SliceImpl<>(content, pageable, false);

        return new SliceImpl<>(content.subList(0, pageable.getPageSize()), pageable, true);
    }

    @Override
    public Slice<Record> findAllByIdAfterAndKeywordsOrderByIdDesc(List<Keyword> keywords, long cursor, Pageable pageable) {
        return null;
    }

    @Override
    public Slice<Record> findAllByUserIdOrderByIdDesc(long userId, long cursor, Pageable pageable) {
        List<Record> content = records.values().stream()
                .filter(record -> record.getId() < cursor && record.getUploader().getId() == userId)
                .sorted(Comparator.comparing(Record::getId).reversed())
                .toList();

        if (content.size() < pageable.getPageSize())
            return new SliceImpl<>(content, pageable, false);

        return new SliceImpl<>(content.subList(0, pageable.getPageSize()), pageable, true);
    }

    @Override
    public Map<Keyword, Long> countAllByUserIdGroupByKeyword(long userId) {
        return Map.of();
    }

    @Override
    public Slice<Record> findAllBySubscribingUserIdOrderByIdDesc(long userId, long cursor, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Long> findMaxId() {
        return records.keySet().stream().max(Long::compareTo);
    }

    @Override
    public Long count() {
        return (long) records.size();
    }
}
