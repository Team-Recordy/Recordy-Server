package org.recordy.server.exhibition.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.common.domain.JpaMetaInfoEntity;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "exhibitions")
@Entity
public class ExhibitionEntity extends JpaMetaInfoEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ExhibitionEntity(
            Long id,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ExhibitionEntity from(Exhibition exhibition) {
        return new ExhibitionEntity(
                exhibition.getId(),
                exhibition.getCreatedAt(),
                exhibition.getUpdatedAt()
        );
    }
}


