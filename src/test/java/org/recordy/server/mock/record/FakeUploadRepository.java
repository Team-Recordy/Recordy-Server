package org.recordy.server.mock.record;

import org.recordy.server.record.domain.UploadEntity;
import org.recordy.server.record.repository.UploadRepository;

import java.util.List;

public class FakeUploadRepository implements UploadRepository {

    @Override
    public List<UploadEntity> findAllByRecordId(long recordId) {
        return List.of();
    }
}
