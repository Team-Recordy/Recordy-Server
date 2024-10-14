package org.recordy.server.place.controller.dto.response;

import java.time.LocalDateTime;

public record PlaceReviewGetResponse(
        Long id,
        String authorName,
        String content,
        int rating,
        LocalDateTime createdAt
) {
}
