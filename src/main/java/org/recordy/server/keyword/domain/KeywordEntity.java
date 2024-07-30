package org.recordy.server.keyword.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.common.domain.JpaMetaInfoEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "keywords")
@Entity
public class KeywordEntity extends JpaMetaInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private Keyword keyword;

    public KeywordEntity(Long id, Keyword keyword) {
        this.id = id;
        this.keyword = keyword;
    }

    public static KeywordEntity from(Keyword keyword) {
        return new KeywordEntity(keyword.getId(), keyword);
    }

    public Keyword toDomain() {
        return keyword;
    }
}
