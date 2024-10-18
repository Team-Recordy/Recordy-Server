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

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Location of(Point point) {
        return new Location(
                null,
                point,
                null,
                null
        );
    }

    public static Location from(LocationEntity entity) {
        return new Location(
                entity.getId(),
                entity.getGeometry(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
