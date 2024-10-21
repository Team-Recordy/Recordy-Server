package org.recordy.server.location.controller.dto.response;

import lombok.Getter;
import org.locationtech.jts.geom.Point;

@Getter
public class LocationGetResponse {

    Long id;
    double longitude;
    double latitude;

    public LocationGetResponse(
            Long id,
            Point point
    ) {
        this.id = id;
        this.longitude = point.getX();
        this.latitude = point.getY();
    }
}
