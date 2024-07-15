package org.recordy.server.record.domain;

public record File (
        String videoUrl,
        String thumbnailUrl) {

    public File(String videoUrl, String thumbnailUrl) {
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public static File of(String videoUrl, String thumbnailUrl) {
        return new File(videoUrl, thumbnailUrl);
    }
}
