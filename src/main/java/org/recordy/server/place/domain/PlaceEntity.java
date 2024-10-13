package org.recordy.server.place.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.common.domain.JpaMetaInfoEntity;
import org.recordy.server.exhibition.domain.ExhibitionEntity;
import org.recordy.server.location.domain.LocationEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "places")
@Entity
public class PlaceEntity extends JpaMetaInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExhibitionEntity> exhibitions = new ArrayList<>();
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private LocationEntity location;

    private PlaceEntity(
            Long id,
            String name,
            List<ExhibitionEntity> exhibitions,
            LocationEntity location,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.exhibitions = exhibitions;
        this.location = location;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private PlaceEntity(Long id) {
        this.id = id;
    }

    public static PlaceEntity create(Place place) {
        return new PlaceEntity(
                place.getId(),
                place.getName(),
                place.getExhibitions().stream()
                        .map(ExhibitionEntity::from)
                        .toList(),
                LocationEntity.from(place.getLocation()),
                place.getCreatedAt(),
                place.getUpdatedAt()
        );
    }

    public static PlaceEntity from(Place place) {
        return new PlaceEntity(place.getId());
    }
}
