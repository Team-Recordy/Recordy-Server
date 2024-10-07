package org.recordy.server.place.service.dto;

public record Review(
        String author_name,
        String text,
        int rating,
        long time
) {
}
