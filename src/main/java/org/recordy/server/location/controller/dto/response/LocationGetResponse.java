package org.recordy.server.location.controller.dto.response;

public record LocationGetResponse(
        Long id,
        Double latitude,
        Double longitude,
        String formatted,
        String sido,
        String gugun
) {
}
