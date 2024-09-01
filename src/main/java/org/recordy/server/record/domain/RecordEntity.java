package org.recordy.server.record.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.common.domain.JpaMetaInfoEntity;
import org.recordy.server.record.service.dto.FileUrl;
import org.recordy.server.bookmark.domain.BookmarkEntity;
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

    private String videoUrl;
    private String thumbnailUrl;
    private String location;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<UploadEntity> uploads = new ArrayList<>();

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ViewEntity> views = new ArrayList<>();

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<BookmarkEntity> bookmarks = new ArrayList<>();

    @Builder
    public RecordEntity(Long id, String videoUrl, String thumbnailUrl, String location, String content, UserEntity user, LocalDateTime createdAt) {
        this.id = id;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.location = location;
        this.content = content;
        this.user = user;
        this.createdAt = createdAt;
    }

    public static RecordEntity from(Record record) {
        RecordEntity recordEntity = new RecordEntity(
                record.getId(),
                record.getFileUrl().videoUrl(),
                record.getFileUrl().thumbnailUrl(),
                record.getLocation(),
                record.getContent(),
                UserEntity.from(record.getUploader()),
                record.getCreatedAt()
        );
        recordEntity.user.addRecord(recordEntity);

        return recordEntity;
    }

    public Record toDomain() {
        return Record.builder()
                .id(id)
                .fileUrl(new FileUrl(
                        videoUrl,
                        thumbnailUrl
                ))
                .location(location)
                .content(content)
                .uploader(user.toDomain())
                .bookmarkCount(bookmarks.size())
                .createdAt(createdAt)
                .build();
    }

    public void addUpload(UploadEntity upload) {
        uploads.add(upload);
    }

    public void addView(ViewEntity view) {
        views.add(view);
    }

    public void addBookmark(BookmarkEntity bookmark) {
        bookmarks.add(bookmark);
    }
}
