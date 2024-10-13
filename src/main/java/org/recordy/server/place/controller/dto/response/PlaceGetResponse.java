package org.recordy.server.place.controller.dto.response;

import org.recordy.server.location.controller.dto.response.LocationGetResponse;

public record PlaceGetResponse(
        Long id,
        String name,
        long exhibitionSize,
        LocationGetResponse location
) {
}
