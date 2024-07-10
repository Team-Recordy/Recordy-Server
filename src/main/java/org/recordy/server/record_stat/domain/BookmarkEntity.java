package org.recordy.server.record_stat.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.common.domain.JpaMetaInfoEntity;
import org.recordy.server.record.domain.RecordEntity;
import org.recordy.server.user.domain.UserEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "bookmarks")
@Entity
public class BookmarkEntity extends JpaMetaInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private RecordEntity record;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder
    public BookmarkEntity(Long id, RecordEntity record, UserEntity user) {
        this.id = id;
        this.record = record;
        this.user = user;
    }

    public static BookmarkEntity from(Bookmark bookmark) {
        return BookmarkEntity.builder()
                .id(bookmark.getId())
                .record(RecordEntity.from(bookmark.getRecord()))
                .user(UserEntity.from(bookmark.getUser()))
                .build();
    }

    public Bookmark toDomain() {
        return Bookmark.builder()
                .id(id)
                .record(record.toDomain())
                .user(user.toDomain())
                .build();
    }
}
