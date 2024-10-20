package org.recordy.server.mock.record;

import org.recordy.server.record.service.S3Service;
import org.recordy.server.record.domain.FileUrl;
import org.recordy.server.util.DomainFixture;

public class FakeS3Service implements S3Service {

    @Override
    public String generateProfileImageUrl() {
        return DomainFixture.USER_PROFILE_IMAGE_URL;
    }

    @Override
    public FileUrl generateFilePresignedUrl() {
        return FileUrl.of(
                DomainFixture.VIDEO_URL,
                DomainFixture.THUMBNAIL_URL
        );
    }

    @Override
    public FileUrl convertToCloudFrontUrl(FileUrl fileUrl) {
        return FileUrl.of(
                fileUrl.videoUrl(),
                fileUrl.thumbnailUrl()
        );
    }
}
