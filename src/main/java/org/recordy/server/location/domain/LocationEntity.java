package org.recordy.server.location.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.recordy.server.common.domain.JpaMetaInfoEntity;
import org.recordy.server.place.domain.PlaceEntity;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "locations")
@Entity
public class LocationEntity extends JpaMetaInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Point geometry;
    @Embedded
    private Address address;

    @OneToOne(mappedBy = "location")
    private PlaceEntity place;

    private LocationEntity(
            Long id,
            Point geometry,
            Address address,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.geometry = geometry;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static LocationEntity from(Location location) {
        return new LocationEntity(
                location.getId(),
                location.getGeometry(),
                location.getAddress(),
                location.getCreatedAt(),
                location.getUpdatedAt()
        );
    }
}
