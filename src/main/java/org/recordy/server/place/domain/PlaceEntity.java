package org.recordy.server.place.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.common.domain.JpaMetaInfoEntity;
import org.recordy.server.exhibition.domain.ExhibitionEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "places")
@Entity
public class PlaceEntity extends JpaMetaInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "place")
    private final List<ExhibitionEntity> exhibitions = new ArrayList<>();

    private PlaceEntity(
            Long id,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PlaceEntity from(Place place) {
        return new PlaceEntity(
                place.getId(),
                place.getCreatedAt(),
                place.getUpdatedAt()
        );
    }
}
