package org.recordy.server.external.controller;

import lombok.RequiredArgsConstructor;
import org.recordy.server.external.service.impl.S3ServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class S3TestController implements S3TestApi {

    private final S3ServiceImpl s3ServiceImpl;
    private static final String directoryPath = "recordy/file";

    @Override
    @PostMapping(path = "/uploadImageTest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadImageTest(@RequestPart MultipartFile image) throws IOException {
        return this.s3ServiceImpl.uploadImage(image);
    }

    @Override
    @PostMapping(path = "/uploadVideoTest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadVideoTest(@RequestPart MultipartFile video) throws IOException {
        return this.s3ServiceImpl.uploadVideo(video);
    }
}
