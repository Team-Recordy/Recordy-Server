package org.recordy.server.record.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {

    // command
    public String uploadFile(MultipartFile file) throws IOException;
    void deleteFile(String key) throws IOException;

    // query


}
