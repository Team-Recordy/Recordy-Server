package org.recordy.server.record.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {

    // command
    String generatePresignedUrl(String directory);
    String getPresignUrl(String filename);
    void uploadFile(byte[] fileData, String fileName) throws IOException;

    // query


}
