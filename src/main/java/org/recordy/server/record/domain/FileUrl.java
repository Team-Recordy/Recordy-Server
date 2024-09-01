package org.recordy.server.record.domain;

public record FileUrl(
        String videoUrl,
        String thumbnailUrl
) {

    public static FileUrl of(String videoUrl, String thumbnailUrl) {
        return new FileUrl(videoUrl, thumbnailUrl);
    }
}
