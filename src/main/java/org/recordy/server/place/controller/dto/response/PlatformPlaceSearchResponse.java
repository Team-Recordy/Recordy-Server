package org.recordy.server.place.controller.dto.response;

import org.recordy.server.place.service.dto.google.GooglePlaceSearch;

public record PlatformPlaceSearchResponse(
        String platformPlaceId,
        String address
) {

    public static PlatformPlaceSearchResponse from(GooglePlaceSearch google) {
        return new PlatformPlaceSearchResponse(
                google.place_id(),
                google.formatted_address()
        );
    }
}
