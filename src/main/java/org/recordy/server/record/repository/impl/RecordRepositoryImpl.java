package org.recordy.server.record.repository.impl;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.recordy.server.keyword.repository.impl.KeywordJpaRepository;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.RecordEntity;
import org.recordy.server.record.domain.UploadEntity;
import org.recordy.server.record.exception.RecordException;
import org.recordy.server.record.repository.RecordRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class RecordRepositoryImpl implements RecordRepository {

    private final RecordJpaRepository recordJpaRepository;
    private final RecordQueryDslRepository recordQueryDslRepository;
    private final KeywordJpaRepository keywordJpaRepository;
    private final UploadJpaRepository uploadJpaRepository;

    @Transactional
    @Override
    public Record save(Record record) {
        RecordEntity recordEntity = recordJpaRepository.save(RecordEntity.from(record));
        saveUploads(recordEntity, record.getKeywords());

        return recordEntity.toDomain();
    }

    private void saveUploads(RecordEntity recordEntity, List<Keyword> keywords) {
        List<KeywordEntity> keywordEntities = keywordJpaRepository.findAll();
        List<UploadEntity> uploadEntities = keywordEntities.stream()
                .filter(keyword -> keywords.contains(keyword.toDomain()))
                .map(keyword -> UploadEntity.of(recordEntity, keyword))
                .toList();

        uploadJpaRepository.saveAll(uploadEntities);
    }

    @Override
    public void deleteById(long recordId) {
        RecordEntity recordEntity = recordJpaRepository.findById(recordId)
                .orElseThrow(() -> new RecordException(ErrorMessage.RECORD_NOT_FOUND));

        recordJpaRepository.delete(recordEntity);
    }

    @Override
    public Optional<Record> findById(long recordId) {
        return recordJpaRepository.findById(recordId)
                .map(RecordEntity::toDomain);
    }

    @Override
    public Slice<Record> findAllOrderByPopularity(Pageable pageable) {
        return recordQueryDslRepository.findAllOrderByPopularity(pageable)
                .map(RecordEntity::toDomain);
    }

    @Override
    public Slice<Record> findAllByKeywordsOrderByPopularity(List<Keyword> keywords, Pageable pageable) {
        List<KeywordEntity> keywordEntities = keywordJpaRepository.findAll().stream()
                .filter(keyword -> keywords.contains(keyword.toDomain()))
                .toList();

        return recordQueryDslRepository.findAllByKeywordsOrderByPopularity(keywordEntities, pageable)
                .map(RecordEntity::toDomain);
    }

    @Override
    public Slice<Record> findAllByIdAfterOrderByIdDesc(long cursor, Pageable pageable) {
        return recordQueryDslRepository.findAllByIdAfterOrderByIdDesc(cursor, pageable)
                .map(RecordEntity::toDomain);
    }

    @Override
    public Slice<Record> findAllByIdAfterAndKeywordsOrderByIdDesc(List<Keyword> keywords, long cursor, Pageable pageable) {
        List<KeywordEntity> keywordEntities = keywordJpaRepository.findAll().stream()
                .filter(keyword -> keywords.contains(keyword.toDomain()))
                .toList();

        return recordQueryDslRepository.findAllByIdAfterAndKeywordsOrderByIdDesc(keywordEntities, cursor, pageable)
                .map(RecordEntity::toDomain);
    }

    @Override
    public Slice<Record> findAllByUserIdOrderByIdDesc(long userId, long cursor, Pageable pageable) {
        return recordQueryDslRepository.findAllByUserIdOrderByIdDesc(userId, cursor, pageable)
                .map(RecordEntity::toDomain);
    }

    @Override
    public Map<Keyword, Long> countAllByUserIdGroupByKeyword(long userId) {
        Map<KeywordEntity, Long> preference = recordQueryDslRepository.countAllByUserIdGroupByKeyword(userId);

        return preference.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toDomain(),
                        Map.Entry::getValue
                ));
    }
}
