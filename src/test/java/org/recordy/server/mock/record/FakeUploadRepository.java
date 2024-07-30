package org.recordy.server.mock.record;

import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.domain.UploadEntity;
import org.recordy.server.record.repository.UploadRepository;

import java.util.List;
import java.util.Map;

public class FakeUploadRepository implements UploadRepository {

    @Override
    public List<UploadEntity> findAllByRecordId(long recordId) {
        return List.of();
    }

    @Override
    public Map<Keyword, Long> countAllByUserIdGroupByKeyword(long userId) {
        return Map.of();
    }
}
