package org.recordy.server.record.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.record.domain.UploadEntity;
import org.recordy.server.record.repository.UploadRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class UploadRepositoryImpl implements UploadRepository {

    private final UploadQueryDslRepository uploadQueryDslRepository;

    @Override
    public List<UploadEntity> findAllByRecordId(long recordId) {
        return uploadQueryDslRepository.findAllByRecordId(recordId);
    }
}
