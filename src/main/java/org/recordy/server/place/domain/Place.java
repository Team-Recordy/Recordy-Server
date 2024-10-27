package org.recordy.server.place.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.recordy.server.location.domain.Location;
import org.recordy.server.place.domain.usecase.PlaceCreate;

import java.time.LocalDateTime;

import static org.recordy.server.common.util.DomainUtils.mapIfNotNull;

@AllArgsConstructor
@Getter
public class Place {

    private Long id;
    private String name;
    private String platformId;
    private String address;
    private Location location;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Place(Long id) {
        this.id = id;
    }

    public static Place from(PlaceEntity entity) {
        return new Place(
                entity.getId(),
                entity.getName(),
                entity.getPlatformId(),
                entity.getAddress(),
                mapIfNotNull(entity.getLocation(), Location::from),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static Place from(Long id) {
        return new Place(id);
    }

    public static Place create(PlaceCreate create) {
        return new Place(
                null,
                create.name(),
                create.platformId(),
                create.address(),
                create.location(),
                null,
                null
        );
    }
}
