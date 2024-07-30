package org.recordy.server.record.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.recordy.server.record.domain.UploadEntity;
import org.recordy.server.record.repository.UploadRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class UploadRepositoryImpl implements UploadRepository {

    private final UploadQueryDslRepository uploadQueryDslRepository;

    @Override
    public List<UploadEntity> findAllByRecordId(long recordId) {
        return uploadQueryDslRepository.findAllByRecordId(recordId);
    }

    @Override
    public Map<Keyword, Long> countAllByUserIdGroupByKeyword(long userId) {
        Map<KeywordEntity, Long> result = uploadQueryDslRepository.countAllByUserIdGroupByKeyword(userId);

        return result.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toDomain(),
                        Map.Entry::getValue
                ));
    }
}
