package org.recordy.server.record.service.dto;

import jakarta.persistence.Column;

public record FileUrl(
        @Column(length = 1024)
        String videoUrl,
        @Column(length = 1024)
        String thumbnailUrl
) {
    public static FileUrl of(String videoUrl, String thumbnailUrl) {
        return new FileUrl(videoUrl, thumbnailUrl);
    }
    public String getVideoUrl() {
        return videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
