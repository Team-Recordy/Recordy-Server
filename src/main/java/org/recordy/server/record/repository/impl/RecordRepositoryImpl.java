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
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.user.domain.UserEntity;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.user.repository.impl.UserJpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class RecordRepositoryImpl implements RecordRepository {

    private final RecordJpaRepository recordJpaRepository;
    private final RecordQueryDslRepository recordQueryDslRepository;
    private final KeywordJpaRepository keywordJpaRepository;
    private final UploadJpaRepository uploadJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public Record save(Record record) {
        List<KeywordEntity> keywords = keywordJpaRepository.findAll();
        RecordEntity recordEntity = recordJpaRepository.save(RecordEntity.from(record));

        List<UploadEntity> uploadEntities = keywords.stream()
                .filter(keyword -> record.getKeywords().contains(keyword.toDomain()))
                .map(keyword -> UploadEntity.of(recordEntity, keyword))
                .toList();
        uploadJpaRepository.saveAll(uploadEntities);

        return recordEntity.toDomain();
    }

    @Override
    public void deleteById(long recordId) {
        recordJpaRepository.deleteById(recordId);
    }

    @Override
    public Optional<Record> findById(long recordId) {
        return recordJpaRepository.findById(recordId)
                .map(RecordEntity::toDomain);
    }

    @Override
    public Slice<Record> findAllOrderByPopularity(long cursor, Pageable pageable) {
        return null;
    }

    @Override
    public Slice<Record> findAllByIdAfterOrderByIdDesc(long cursor, Pageable pageable) {
        return recordQueryDslRepository.findAllByIdAfterOrderByIdDesc(cursor, pageable)
                .map(RecordEntity::toDomain);
    }

    @Override
    public Slice<Record> findAllByIdAfterAndKeywordsOrderByIdDesc(List<Keyword> keywords, long cursor, Pageable pageable) {
        List<KeywordEntity> keywordEntities = keywords.stream()
                .map(KeywordEntity::from)
                .toList();

        return recordQueryDslRepository.findAllByIdAfterAndKeywordsOrderByIdDesc(keywordEntities, cursor, pageable)
                .map(RecordEntity::toDomain);
    }

    @Override
    public Slice<Record> findAllByUserIdOrderByIdDesc(long userId, long cursor, Pageable pageable) {
        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorMessage.USER_NOT_FOUND));
        return recordQueryDslRepository.findAllByUserIdOrderByIdDesc(userEntity,cursor, pageable)
                .map(RecordEntity::toDomain);
    }
}
