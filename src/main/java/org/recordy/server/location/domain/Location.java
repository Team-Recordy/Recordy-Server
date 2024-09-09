package org.recordy.server.location.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@AllArgsConstructor(staticName = "create")
@Getter
public class Location {

    private Long id;
    private Point geometry;
    private Address address;
    private String googlePlaceId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Location from(LocationEntity entity) {
        return new Location(
                entity.getId(),
                entity.getGeometry(),
                entity.getAddress(),
                entity.getGooglePlaceId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
