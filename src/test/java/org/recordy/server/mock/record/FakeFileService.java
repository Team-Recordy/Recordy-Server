package org.recordy.server.mock.record;

import org.recordy.server.record.domain.File;
import org.recordy.server.record.service.FileService;
import org.recordy.server.record.controller.dto.FileUrl;
import org.recordy.server.util.DomainFixture;

public class FakeFileService implements FileService {

    @Override
    public FileUrl save(File file) {
        return new FileUrl(
                DomainFixture.VIDEO_URL,
                DomainFixture.THUMBNAIL_URL
        );
    }
}
