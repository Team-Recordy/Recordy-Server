package org.recordy.server.place.controller.dto.request;

public record PlaceCreateRequest(
        String name,
        String address
) {

    public String toQuery() {
        return name + " " + address;
    }
}
