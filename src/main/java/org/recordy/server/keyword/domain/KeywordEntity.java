package org.recordy.server.keyword.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "keywords")
@Entity
public class KeywordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Keyword keyword;

    public KeywordEntity(Keyword keyword) {
        this.keyword = keyword;
    }

    public static KeywordEntity from(Keyword keyword) {
        return new KeywordEntity(keyword);
    }

    public Keyword toDomain() {
        return keyword;
    }
}
