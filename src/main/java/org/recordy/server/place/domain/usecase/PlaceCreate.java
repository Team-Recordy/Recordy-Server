package org.recordy.server.place.domain.usecase;

import org.recordy.server.location.domain.Location;
import org.recordy.server.place.controller.dto.request.PlaceCreateRequest;

public record PlaceCreate(
        String name,
        String address,
        String platformId,
        Location location
) {

    public static PlaceCreate from(PlaceCreateRequest request, Location location) {
        return new PlaceCreate(
                request.name(),
                request.address(),
                request.id(),
                location
        );
    }
}
