package org.recordy.server.exhibition.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.recordy.server.exhibition.domain.usecase.ExhibitionCreate;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Exhibition {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Exhibition from(ExhibitionEntity entity) {
        return new Exhibition(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static Exhibition create(ExhibitionCreate create) {
        return new Exhibition(
                create.id(),
                null,
                null
        );
    }
}
