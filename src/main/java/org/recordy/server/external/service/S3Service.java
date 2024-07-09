package org.recordy.server.external.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {

    // command
    String uploadImage(MultipartFile image) throws IOException;
    String uploadVideo( MultipartFile video) throws IOException;
    void deleteFile(String key) throws IOException;

    // query
    void validateImageExtension(MultipartFile image);
    void validateVideoExtension(MultipartFile video);
    void validateImageSize(MultipartFile image);
    void validateVideoSize(MultipartFile video);
}
