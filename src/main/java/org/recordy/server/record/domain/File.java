package org.recordy.server.record.domain;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record File(
        MultipartFile video,
        MultipartFile thumbnail
) {

    public static File of(MultipartFile video, MultipartFile thumbnail) {
        return new File(video, thumbnail);
    }

    public String generateFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        return UUID.randomUUID() + extension;
    }

    public String getVideoFileName() {
        return generateFileName(video);
    }

    public String getThumbnailFileName() {
        return generateFileName(thumbnail);
    }
}
