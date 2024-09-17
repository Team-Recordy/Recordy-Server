package org.recordy.server.exhibition.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.common.domain.JpaMetaInfoEntity;
import org.recordy.server.place.domain.PlaceEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.recordy.server.common.util.DomainUtils.mapIfNotNull;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "exhibitions")
@Entity
public class ExhibitionEntity extends JpaMetaInfoEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isFree;

    @ManyToOne(fetch = FetchType.LAZY)
    private PlaceEntity place;

    private ExhibitionEntity(
            Long id,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            boolean isFree,
            PlaceEntity place,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isFree = isFree;
        this.place = place;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ExhibitionEntity from(Exhibition exhibition) {
        return new ExhibitionEntity(
                exhibition.getId(),
                exhibition.getName(),
                exhibition.getStartDate(),
                exhibition.getEndDate(),
                exhibition.isFree(),
                mapIfNotNull(exhibition.getPlace(), PlaceEntity::from),
                exhibition.getCreatedAt(),
                exhibition.getUpdatedAt()
        );
    }
}


