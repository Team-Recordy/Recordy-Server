package org.recordy.server.record.repository;

import org.recordy.server.record.domain.UploadEntity;

import java.util.List;

public interface UploadRepository {

    // query
    List<UploadEntity> findAllByRecordId(long recordId);
}
