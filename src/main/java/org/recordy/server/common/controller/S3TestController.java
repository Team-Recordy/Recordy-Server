package org.recordy.server.common.controller;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.service.impl.S3ServiceImpl;
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

    @Override
    @PostMapping(path = "/uploadTest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadTest(@RequestPart MultipartFile file) throws IOException {
        return this.s3ServiceImpl.uploadFile(file);
    }

}
