package org.recordy.server.place.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.recordy.server.place.domain.usecase.PlaceCreate;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Place {

    private Long id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Place from(PlaceEntity entity) {
        return new Place(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static Place create(PlaceCreate create) {
        return new Place(
                create.id(),
                null,
                null
        );
    }
}
