package org.recordy.server.external.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.external.exception.ExternalException;
import org.recordy.server.external.service.impl.S3ServiceImpl;
import org.recordy.server.mock.FakeContainer;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class S3ServicesTest {

    private S3Service s3Service;

    @BeforeEach
    void init() {
        FakeContainer fakeContainer = new FakeContainer();
        s3Service = fakeContainer.s3Service;
    }

    @Test
    void 이미지_업로드_성공() throws IOException {
        // given
        MultipartFile imageMock = mock(MultipartFile.class);
        when(imageMock.getContentType()).thenReturn("image/jpeg");
        when(imageMock.getSize()).thenReturn(1024L);
        when(imageMock.getBytes()).thenReturn(new byte[1024]);

        // when
        String result = s3Service.uploadImage("test-directory", imageMock);

        // then
        assertThatCode(() -> s3Service.uploadImage("test-directory", imageMock)).doesNotThrowAnyException();
    }

    @Test
    void 비디오_업로드_성공() throws IOException {
        // given
        MultipartFile videoMock = mock(MultipartFile.class);
        when(videoMock.getContentType()).thenReturn("video/mp4");
        when(videoMock.getSize()).thenReturn(1024L * 50);
        when(videoMock.getBytes()).thenReturn(new byte[1024 * 50]);

        // when
        String result = s3Service.uploadVideo("test-directory", videoMock);

        // then
        assertThatCode(() -> s3Service.uploadVideo("test-directory", videoMock)).doesNotThrowAnyException();
    }
}
