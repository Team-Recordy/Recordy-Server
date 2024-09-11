package org.recordy.server.place.domain.usecase;

import org.locationtech.jts.geom.Point;
import org.recordy.server.place.service.dto.Review;

import java.util.List;

public record PlaceGoogle(
        Point geometry,
        String formattedAddress,
        String placeId,
        List<Review> reviews,
        String website
) {
}
