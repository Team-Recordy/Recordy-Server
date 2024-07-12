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

    private String name;

    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private Keyword keyword;

    public KeywordEntity(Keyword keyword) {
        this.keyword = keyword;
        this.name = keyword.name();
    }

    public static KeywordEntity from(Keyword keyword) {
        return new KeywordEntity(keyword);
    }

    public Keyword toDomain() {
        return keyword;
    }
}
