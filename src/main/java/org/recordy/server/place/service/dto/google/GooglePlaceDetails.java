package org.recordy.server.place.service.dto.google;

import java.util.List;

public record GooglePlaceDetails(
        String name,
        String formatted_address,
        Geometry geometry,
        String place_id,
        List<Review> reviews
) {
}
