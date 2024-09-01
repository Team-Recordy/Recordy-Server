package org.recordy.server.exhibition.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.recordy.server.exhibition.domain.usecase.ExhibitionCreate;
import org.recordy.server.exhibition.domain.usecase.ExhibitionUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Exhibition {

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Exhibition from(ExhibitionEntity entity) {
        return new Exhibition(
                entity.getId(),
                entity.getName(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static Exhibition create(ExhibitionCreate create) {
        return new Exhibition(
                create.id(),
                create.name(),
                create.startDate(),
                create.endDate(),
                null,
                null
        );
    }

    public Exhibition update(ExhibitionUpdate update) {
        return new Exhibition(
                this.id,
                update.name(),
                update.startDate(),
                update.endDate(),
                this.createdAt,
                null
        );
    }
}
