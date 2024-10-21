package org.recordy.server.place.controller.dto.request;

public record PlaceCreateRequest(
        String id,
        String name,
        double longitude,
        double latitude,
        String address
) {
}
