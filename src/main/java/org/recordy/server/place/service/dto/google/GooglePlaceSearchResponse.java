package org.recordy.server.place.service.dto.google;

import java.util.List;

public record GooglePlaceSearchResponse(
        List<GooglePlaceSearch> candidates
) {
}
