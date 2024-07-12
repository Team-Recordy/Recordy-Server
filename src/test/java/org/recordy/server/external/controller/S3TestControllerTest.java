package org.recordy.server.external.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.record.service.impl.S3ServiceImpl;
import org.recordy.server.mock.FakeContainer;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


public class S3TestControllerTest {

    private S3TestController s3TestController;
    private S3ServiceImpl s3Service;

    @BeforeEach
    void init() {
        FakeContainer fakeContainer = new FakeContainer();
        s3Service = (S3ServiceImpl) fakeContainer.s3Service;
        s3TestController = new S3TestController(s3Service);
    }

    @Test
    void uploadImageTest_이미지_업로드_성공() throws IOException {
        // given
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[1024]);

        // when
        String result = s3TestController.uploadTest(image);

        // then
        assertThat(result).isNotNull();
        assertThat(result).contains("recordy/file/");
    }

    @Test
    void uploadVideoTest_비디오_업로드_성공() throws IOException {
        // given
        MockMultipartFile video = new MockMultipartFile("video", "test.mp4", "video/mp4", new byte[1024 * 50]);

        // when
        String result = s3TestController.uploadTest(video);

        // then
        assertThat(result).isNotNull();
        assertThat(result).contains("recordy/file/");
    }
}

