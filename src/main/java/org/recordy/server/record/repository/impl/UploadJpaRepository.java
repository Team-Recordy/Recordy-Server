package org.recordy.server.record.repository.impl;

import org.recordy.server.record.domain.RecordEntity;
import org.recordy.server.record.domain.UploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UploadJpaRepository extends JpaRepository<UploadEntity, Long> {

    //command
    void deleteAllByRecordUserId(long userId);

    //query
    List<UploadEntity> findAllByRecord(RecordEntity record);
}
