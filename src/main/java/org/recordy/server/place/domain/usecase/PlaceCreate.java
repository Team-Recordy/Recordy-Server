package org.recordy.server.place.domain.usecase;

import org.recordy.server.location.domain.Location;

public record PlaceCreate(
        String name,
        String websiteUrl,
        Location location
) {
}
