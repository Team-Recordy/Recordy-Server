package org.recordy.server.record.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.common.domain.JpaMetaInfoEntity;
import org.recordy.server.bookmark.domain.BookmarkEntity;
import org.recordy.server.place.domain.PlaceEntity;
import org.recordy.server.view.domain.ViewEntity;
import org.recordy.server.user.domain.UserEntity;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "records")
@Entity
public class RecordEntity extends JpaMetaInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private FileUrl fileUrl;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private PlaceEntity place;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ViewEntity> views = new ArrayList<>();

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<BookmarkEntity> bookmarks = new ArrayList<>();

    public RecordEntity(
            Long id,
            FileUrl fileUrl,
            String content,
            UserEntity user,
            PlaceEntity place,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.content = content;
        this.user = user;
        this.place = place;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static RecordEntity from(Record record) {
        return new RecordEntity(
                record.getId(),
                record.getFileUrl(),
                record.getContent(),
                UserEntity.from(record.getUploader()),
                PlaceEntity.from(record.getPlace()),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }

    public Record toDomain() {
        return Record.builder()
                .id(id)
                .fileUrl(fileUrl)
                .content(content)
                .uploader(user.toDomain())
                .bookmarkCount(bookmarks.size())
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public void addView(ViewEntity view) {
        views.add(view);
    }

    public void addBookmark(BookmarkEntity bookmark) {
        bookmarks.add(bookmark);
    }
}
