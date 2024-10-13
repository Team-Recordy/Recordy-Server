package org.recordy.server.place.controller.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.recordy.server.location.controller.dto.response.LocationGetResponse;

@Getter
public class PlaceGetResponse {

    Long id;
    String name;
    LocationGetResponse location;
    @Setter
    long exhibitionSize;
    @Setter
    long recordSize;

    public PlaceGetResponse(
            Long id,
            String name,
            LocationGetResponse location
    ) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
}
