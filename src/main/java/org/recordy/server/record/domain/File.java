package org.recordy.server.record.domain;

import org.springframework.web.multipart.MultipartFile;

public record File(
        MultipartFile video,
        MultipartFile thumbnail
) {

    public static File of(MultipartFile video, MultipartFile thumbnail) {
        return new File(video, thumbnail);
    }
}
