package org.recordy.server.place.service.dto;

public record GooglePlaceSearch(
        Geometry geometry,
        String formatted_address,
        String place_id
) {
}
