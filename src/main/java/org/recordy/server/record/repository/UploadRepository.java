package org.recordy.server.record.repository;

import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.domain.UploadEntity;

import java.util.List;
import java.util.Map;

public interface UploadRepository {

    // query
    List<UploadEntity> findAllByRecordId(long recordId);
    Map<Keyword, Long> countAllByUserIdGroupByKeyword(long userId);
}
