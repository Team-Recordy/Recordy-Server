package org.recordy.server.exhibition.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.recordy.server.exhibition.domain.usecase.ExhibitionCreate;
import org.recordy.server.exhibition.domain.usecase.ExhibitionUpdate;
import org.recordy.server.place.domain.Place;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.recordy.server.common.util.DomainUtils.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Exhibition {

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isFree;
    private String url;
    private Place place;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Exhibition from(ExhibitionEntity entity) {
        return new Exhibition(
                entity.getId(),
                entity.getName(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.isFree(),
                entity.getUrl(),
                mapIfNotNull(entity.getPlace(), Place::from),
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
                create.isFree(),
                create.url(),
                create.place(),
                null,
                null
        );
    }

    public Exhibition update(ExhibitionUpdate update) {
        return new Exhibition(
                this.id,
                updateIfNotEmpty(this.name, update.name()),
                updateIfNotNull(this.startDate, update.startDate()),
                updateIfNotNull(this.endDate, update.startDate()),
                updateIfNotNull(this.isFree, update.isFree()),
                updateIfNotEmpty(this.url, update.url()),
                this.place,
                this.createdAt,
                null
        );
    }
}
