package org.recordy.server.place.domain.usecase;

import org.locationtech.jts.geom.Point;
import org.recordy.server.place.service.dto.google.GooglePlaceDetails;
import org.recordy.server.place.service.dto.Review;

import java.util.List;

public record PlatformPlace(
        Point geometry,
        String address,
        String placeId,
        List<Review> reviews
) {

    public static PlatformPlace of(Point geometry, GooglePlaceDetails placeDetails) {
        return new PlatformPlace(
                geometry,
                placeDetails.formatted_address(),
                placeDetails.place_id(),
                placeDetails.reviews()
        );
    }
}
