package org.recordy.server.record.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.common.config.S3Config;
import org.recordy.server.common.exception.ExternalException;
import org.recordy.server.record.service.impl.S3ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class S3ServiceTest {

    private S3ServiceImpl s3Service;
    private S3Client s3ClientMock;
    private S3Presigner presignerMock;

    @Value("${aws-property.s3-bucket-name}")
    private String bucket = "recordy-bucket";

    @BeforeEach
    void init() {
        s3ClientMock = mock(S3Client.class);
        presignerMock = mock(S3Presigner.class);
        s3Service = new S3ServiceImpl(s3ClientMock, presignerMock);
    }

    @Test
    void 이미지_업로드_성공() throws IOException {
        // given
        MultipartFile imageMock = mock(MultipartFile.class);
        when(imageMock.getContentType()).thenReturn("image/jpeg");
        when(imageMock.getSize()).thenReturn(1024L);
        when(imageMock.getBytes()).thenReturn(new byte[1024]);
        when(s3ClientMock.putObject(any(PutObjectRequest.class), any(software.amazon.awssdk.core.sync.RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        // when
        String result = s3Service.uploadFile(imageMock.getBytes(), "test-image.jpg");

        // then
        assertThatCode(() -> s3Service.uploadFile(imageMock.getBytes(), "test-image.jpg")).doesNotThrowAnyException();
    }

    @Test
    void 비디오_업로드_성공() throws IOException {
        // given
        MultipartFile videoMock = mock(MultipartFile.class);
        when(videoMock.getContentType()).thenReturn("video/mp4");
        when(videoMock.getSize()).thenReturn(1024L * 50);
        when(videoMock.getBytes()).thenReturn(new byte[1024 * 50]);
        when(s3ClientMock.putObject(any(PutObjectRequest.class), any(software.amazon.awssdk.core.sync.RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        // when
        String result = s3Service.uploadFile(videoMock.getBytes(), "test-video.mp4");

        // then
        assertThatCode(() -> s3Service.uploadFile(videoMock.getBytes(), "test-video.mp4")).doesNotThrowAnyException();
    }

    @Test
    void 이미지_업로드_실패_파일_확장자_유효성_검사_실패() throws IOException {
        // given
        MultipartFile imageMock = mock(MultipartFile.class);
        when(imageMock.getContentType()).thenReturn("image/mov");
        when(imageMock.getSize()).thenReturn(1024L);
        when(imageMock.getBytes()).thenReturn(new byte[1024]);

        //when
        //then
        assertThatThrownBy(() -> s3Service.uploadFile(imageMock.getBytes(), "test-image.mov"))
                .isInstanceOf(ExternalException.class);
    }

    @Test
    void 비디오_업로드_실패_파일_확장자_유효성_검사_실패() throws IOException {
        // given
        MultipartFile videoMock = mock(MultipartFile.class);
        when(videoMock.getContentType()).thenReturn("video/hwp");
        when(videoMock.getSize()).thenReturn(1024L * 50);
        when(videoMock.getBytes()).thenReturn(new byte[1024 * 50]);

        //when
        //then
        assertThatThrownBy(() -> s3Service.uploadFile(videoMock.getBytes(), "test-video.hwp"))
                .isInstanceOf(ExternalException.class);
    }

    @Test
    void 이미지_업로드_실패_파일_크기_유효성_검사_실패() throws IOException {
        // given
        long maxFileSize = 50 * 1024L;
        MultipartFile imageMock = mock(MultipartFile.class);
        when(imageMock.getContentType()).thenReturn("image/jpg");
        when(imageMock.getSize()).thenReturn(1024L * 1024 * 200);

        //when
        //then
        assertThatThrownBy(() -> s3Service.uploadFile(imageMock.getBytes(), "test-image.jpg"))
                .isInstanceOf(ExternalException.class);
    }

    @Test
    void 비디오_업로드_실패_파일_크기_유효성_검사_실패() throws IOException {
        // given
        MultipartFile videoMock = mock(MultipartFile.class);
        when(videoMock.getContentType()).thenReturn("video/mov");
        when(videoMock.getSize()).thenReturn(1024L * 1024 * 200);
        when(videoMock.getBytes()).thenReturn(new byte[1024 * 1024 * 200]);

        //when
        //then
        assertThatThrownBy(() -> s3Service.uploadFile(videoMock.getBytes(), "test-video.mov"))
                .isInstanceOf(ExternalException.class);
    }
}
