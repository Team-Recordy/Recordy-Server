package org.recordy.server.place.service.dto.google;

public record GooglePlaceSearch(
        String place_id,
        String formatted_address
) {
}
