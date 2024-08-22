package org.recordy.server.record.service;

import org.junit.jupiter.api.Test;
import org.recordy.server.record.service.dto.FileUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class S3ServiceTest {

    @Autowired
    private S3Service sut;

    @Value("${aws-property.s3-bucket-name}")
    private String s3Domain;

    @Value("${aws-property.cloudfront-domain-name}")
    private String cloudFrontDomain;

    @Test
    void 알맞은_디렉토리와_확장자를_가진_presigned_url을_생성할_수_있다() {
        // when
        FileUrl fileUrl = sut.generatePresignedUrl();
        // then
        assertAll(
                () -> assertThat(fileUrl.videoUrl()).contains("videos/"),
                () -> assertThat(fileUrl.videoUrl()).contains(".mp4"),
                () -> assertThat(fileUrl.thumbnailUrl()).contains("thumbnails/"),
                () -> assertThat(fileUrl.thumbnailUrl()).contains(".jpeg")
        );
    }

    @Test
    void S3_도메인이_포함되어_있는_presigned_url을_생성할_수_있다() {
        // when
        FileUrl fileUrl = sut.generatePresignedUrl();
        // then
        assertAll(
                () -> assertThat(fileUrl.videoUrl()).contains(s3Domain),
                () -> assertThat(fileUrl.thumbnailUrl()).contains(s3Domain)
        );
    }

    @Test
    void presigned_url에서_s3_도메인을_cloudfront_도메인으로_변경할_수_있다() {
        // given
        FileUrl fileUrl = sut.generatePresignedUrl();

        // when
        FileUrl convertedFileUrl = sut.convertToCloudFrontUrl(fileUrl);

        // then
        assertAll(
                () -> assertThat(convertedFileUrl.videoUrl()).contains(cloudFrontDomain),
                () -> assertThat(convertedFileUrl.thumbnailUrl()).contains(cloudFrontDomain)
        );
    }
}