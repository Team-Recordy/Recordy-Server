package org.recordy.server.place.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.location.domain.Location;
import org.recordy.server.place.domain.usecase.PlaceCreate;

import java.time.LocalDateTime;
import java.util.List;

import static org.recordy.server.common.util.DomainUtils.mapIfNotNull;

@AllArgsConstructor
@Getter
public class Place {

    private Long id;
    private String name;
    private List<Exhibition> exhibitions;
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
                entity.getExhibitions().stream()
                        .map(Exhibition::from)
                        .toList(),
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
                List.of(),
                create.location(),
                null,
                null
        );
    }
}
