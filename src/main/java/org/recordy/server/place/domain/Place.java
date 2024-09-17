package org.recordy.server.place.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.recordy.server.exhibition.domain.Exhibition;
import org.recordy.server.location.domain.Location;
import org.recordy.server.place.domain.usecase.PlaceCreate;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class Place {

    private Long id;
    private String name;
    private String websiteUrl;
    private List<Exhibition> exhibitions;
    private Location location;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Place from(PlaceEntity entity) {
        return new Place(
                entity.getId(),
                entity.getName(),
                entity.getWebsiteUrl(),
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
                null,
                create.name(),
                create.websiteUrl(),
                List.of(),
                create.location(),
                null,
                null
        );
    }
}
