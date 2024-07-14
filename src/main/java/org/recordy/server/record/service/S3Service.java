package org.recordy.server.record.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {

    // command
    public String getPresignUrl(String filename);
    String uploadFile(byte[] fileData, String fileName) throws IOException;

    // query


}
