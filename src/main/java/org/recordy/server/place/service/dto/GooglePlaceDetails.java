package org.recordy.server.place.service.dto;

import java.util.List;

public record GooglePlaceDetails(
        String formatted_address,
        Geometry geometry,
        String place_id,
        List<Review> reviews
) {
}
