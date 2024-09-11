package org.recordy.server.place.service.dto;

import java.util.List;

public record GooglePlaceSearchResponse(
        List<GooglePlaceSearch> candidates,
        String status
) {
}
