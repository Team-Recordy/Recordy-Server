package org.recordy.server.place.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.common.domain.JpaMetaInfoEntity;
import org.recordy.server.location.domain.LocationEntity;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "places")
@Entity
public class PlaceEntity extends JpaMetaInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true, nullable = false)
    private String platformId;
    private String address;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private LocationEntity location;

    private PlaceEntity(
            Long id,
            String name,
            String platformId,
            String address,
            LocationEntity location,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.platformId = platformId;
        this.address = address;
        this.location = location;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private PlaceEntity(Long id) {
        this.id = id;
    }

    public static PlaceEntity create(Place place) {
        return new PlaceEntity(
                place.getId(),
                place.getName(),
                place.getPlatformId(),
                place.getAddress(),
                LocationEntity.from(place.getLocation()),
                place.getCreatedAt(),
                place.getUpdatedAt()
        );
    }

    public static PlaceEntity from(Place place) {
        return new PlaceEntity(place.getId());
    }
}
