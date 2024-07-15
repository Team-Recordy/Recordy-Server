package org.recordy.server.record.domain;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record File(
        String videoUrl,
        String thumbnailUrl
) {

    public static File of(String videoUrl, String thumbnailUrl) {
        return new File(videoUrl, thumbnailUrl);
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

}
