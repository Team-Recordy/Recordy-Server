package org.recordy.server.location.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class Location {

    private Long id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Location from(LocationEntity entity) {
        return new Location(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
