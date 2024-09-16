package org.recordy.server.place.controller.dto.request;

public record PlaceCreateRequest(
        String name,
        String sido,
        String gugun
) {

    public String toQuery() {
        return name + " " + sido + " " + gugun;
    }
}
