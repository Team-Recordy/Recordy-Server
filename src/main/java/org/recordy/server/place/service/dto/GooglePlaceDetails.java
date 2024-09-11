package org.recordy.server.place.service.dto;

import java.util.List;

public record GooglePlaceDetails(
        List<Review> reviews,
        String website
) {
}
