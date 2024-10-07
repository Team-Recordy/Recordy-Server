package org.recordy.server.place.domain.usecase;

import org.locationtech.jts.geom.Point;
import org.recordy.server.place.service.dto.GooglePlaceDetails;
import org.recordy.server.place.service.dto.Review;

import java.util.List;

public record PlaceGoogle(
        Point geometry,
        String address,
        String placeId,
        List<Review> reviews,
        String website
) {

    public static PlaceGoogle of(Point geometry, GooglePlaceDetails placeDetails) {
        return new PlaceGoogle(
                geometry,
                placeDetails.formatted_address(),
                placeDetails.place_id(),
                placeDetails.reviews(),
                placeDetails.website()
        );
    }
}
