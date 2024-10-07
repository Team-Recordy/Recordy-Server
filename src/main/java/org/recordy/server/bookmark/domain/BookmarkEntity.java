package org.recordy.server.bookmark.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.common.domain.JpaMetaInfoEntity;
import org.recordy.server.record.domain.Record;
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
    public BookmarkEntity(Long id, RecordEntity record, UserEntity user, LocalDateTime createdAt) {
        this.id = id;
        this.record = record;
        this.user = user;
    }

    public static BookmarkEntity from(Bookmark bookmark) {
        BookmarkEntity bookmarkEntity = BookmarkEntity.builder()
                .id(bookmark.getId())
                .record(RecordEntity.from(bookmark.getRecord()))
                .user(UserEntity.from(bookmark.getUser()))
                .createdAt(bookmark.getCreatedAt())
                .build();
        bookmarkEntity.getRecord().addBookmark(bookmarkEntity);

        return bookmarkEntity;
    }

    public Bookmark toDomain() {
        return Bookmark.builder()
                .id(id)
                .record(Record.from(record))
                .user(user.toDomain())
                .createdAt(createdAt)
                .build();
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
