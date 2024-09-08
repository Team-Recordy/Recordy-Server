package org.recordy.server.location.controller.dto.response;

import org.locationtech.jts.geom.Point;

public record LocationGetResponse(
        Long id,
        Point point,
        String formatted,
        String sido,
        String gugun
) {
}
