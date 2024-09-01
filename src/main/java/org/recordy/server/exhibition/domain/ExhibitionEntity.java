package org.recordy.server.exhibition.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.common.domain.JpaMetaInfoEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private ExhibitionEntity(
            Long id,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ExhibitionEntity from(Exhibition exhibition) {
        return new ExhibitionEntity(
                exhibition.getId(),
                exhibition.getName(),
                exhibition.getStartDate(),
                exhibition.getEndDate(),
                exhibition.getCreatedAt(),
                exhibition.getUpdatedAt()
        );
    }
}


