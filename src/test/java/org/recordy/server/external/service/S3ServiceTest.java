package org.recordy.server.external.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.common.config.S3Config;
import org.recordy.server.common.exception.ExternalException;
import org.recordy.server.record.service.impl.S3ServiceImpl;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class S3ServiceTest {

    private S3ServiceImpl s3Service;
    private S3Client s3ClientMock;

    @BeforeEach
    void init() {
        s3Service = new S3ServiceImpl("recordy-bucket", new S3Config("access-key", "secret-key", "ap-northeast-2"));
        s3ClientMock = mock(S3Client.class);
        s3Service.setS3Client(s3ClientMock);
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
        String result = s3Service.uploadFile(imageMock);

        // then
        assertThatCode(() -> s3Service.uploadFile(imageMock)).doesNotThrowAnyException();
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
        String result = s3Service.uploadFile(videoMock);

        // then
        assertThatCode(() -> s3Service.uploadFile(videoMock)).doesNotThrowAnyException();
    }

    @Test
    void 이미지_업로드_실패_파일_확장자_유효성_검사_실패() throws IOException {
        // given
        MultipartFile imageMock = mock(MultipartFile.class);
        when(imageMock.getContentType()).thenReturn("image/mov");
        when(imageMock.getSize()).thenReturn(1024L);
        when(imageMock.getBytes()).thenReturn(new byte[1024]);
        when(s3ClientMock.putObject(any(PutObjectRequest.class), any(software.amazon.awssdk.core.sync.RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        //when
        //then
        assertThatThrownBy(() -> s3Service.uploadFile(imageMock))
                .isInstanceOf(ExternalException.class);
    }

    @Test
    void 비디오_업로드_실패_파일_확장자_유효성_검사_실패() throws IOException {
        // given
        MultipartFile videoMock = mock(MultipartFile.class);
        when(videoMock.getContentType()).thenReturn("video/hwp");
        when(videoMock.getSize()).thenReturn(1024L * 50);
        when(videoMock.getBytes()).thenReturn(new byte[1024 * 50]);
        when(s3ClientMock.putObject(any(PutObjectRequest.class), any(software.amazon.awssdk.core.sync.RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        //when
        //then
        assertThatThrownBy(() -> s3Service.uploadFile(videoMock))
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
        assertThatThrownBy(() -> s3Service.uploadFile(imageMock))
                .isInstanceOf(ExternalException.class);
    }

    @Test
    void 비디오_업로드_실패_파일_크기_유효성_검사_실패() throws IOException {
        // given
        MultipartFile videoMock = mock(MultipartFile.class);
        when(videoMock.getContentType()).thenReturn("video/mov");
        when(videoMock.getSize()).thenReturn(1024L * 1024 * 200);
        when(s3ClientMock.putObject(any(PutObjectRequest.class), any(software.amazon.awssdk.core.sync.RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        //when
        //then
        assertThatThrownBy(() -> s3Service.uploadFile(videoMock))
                .isInstanceOf(ExternalException.class);
    }
}
