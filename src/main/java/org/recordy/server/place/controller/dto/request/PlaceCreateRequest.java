package org.recordy.server.place.controller.dto.request;

public record PlaceCreateRequest(
        String name,
        String area
) {

    public String toQuery() {
        return name + " " + area;
    }
}
