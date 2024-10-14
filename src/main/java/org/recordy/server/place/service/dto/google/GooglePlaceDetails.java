package org.recordy.server.place.service.dto.google;

import org.recordy.server.place.service.dto.Review;

import java.util.List;

public record GooglePlaceDetails(
        String formatted_address,
        Geometry geometry,
        String place_id,
        List<Review> reviews
) {
}
