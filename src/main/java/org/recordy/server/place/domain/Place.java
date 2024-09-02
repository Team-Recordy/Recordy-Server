package org.recordy.server.place.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.location.domain.Location;
import org.recordy.server.place.domain.usecase.PlaceCreate;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Place {

    private Long id;
    private List<Exhibition> exhibitions;
    private Location location;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Place from(PlaceEntity entity) {
        return new Place(
                entity.getId(),
                entity.getExhibitions().stream()
                        .map(Exhibition::from)
                        .toList(),
                Location.from(entity.getLocation()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static Place create(PlaceCreate create) {
        return new Place(
                create.id(),
                List.of(),
                create.location(),
                null,
                null
        );
    }

    public static Place create(PlaceCreate create, List<Exhibition> exhibitions) {
        return new Place(
                create.id(),
                exhibitions,
                create.location(),
                null,
                null
        );
    }
}
