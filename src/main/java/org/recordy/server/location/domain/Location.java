package org.recordy.server.location.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.locationtech.jts.geom.Point;
import org.recordy.server.place.domain.usecase.PlatformPlace;

import java.time.LocalDateTime;

@AllArgsConstructor(staticName = "create")
@Getter
public class Location {

    private Long id;
    private Point geometry;
    private String address;
    private String platformPlaceId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Location of(PlatformPlace platformPlace) {
        return new Location(
                null,
                platformPlace.geometry(),
                platformPlace.address(),
                platformPlace.placeId(),
                null,
                null
        );
    }

    public static Location from(LocationEntity entity) {
        return new Location(
                entity.getId(),
                entity.getGeometry(),
                entity.getAddress(),
                entity.getPlatformPlaceId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
