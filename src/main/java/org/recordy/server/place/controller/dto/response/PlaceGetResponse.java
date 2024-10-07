package org.recordy.server.place.controller.dto.response;

import org.recordy.server.exhibition.controller.dto.response.ExhibitionGetResponse;
import org.recordy.server.location.controller.dto.response.LocationGetResponse;

import java.util.List;

public record PlaceGetResponse(
        Long id,
        String name,
        String websiteUrl,
        List<ExhibitionGetResponse> exhibitions,
        LocationGetResponse location
) {
}
