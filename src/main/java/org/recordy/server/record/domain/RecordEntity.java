package org.recordy.server.record.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.recordy.server.record.service.dto.FileUrl;
import org.recordy.server.user.domain.UserEntity;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "records")
@Entity
public class RecordEntity {

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
    private List<UploadEntity> uploads = new ArrayList<>();

    @Builder
    public RecordEntity(Long id, String videoUrl, String thumbnailUrl, String location, String content, UserEntity user) {
        this.id = id;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.location = location;
        this.content = content;
        this.user = user;
    }

    public static RecordEntity from(Record record) {
        RecordEntity recordEntity = new RecordEntity(
                record.getId(),
                record.getFileUrl().videoUrl(),
                record.getFileUrl().thumbnailUrl(),
                record.getLocation(),
                record.getContent(),
                UserEntity.from(record.getUploader())
        );

        record.keywords.stream()
                .map(KeywordEntity::from)
                .map(keywordEntity -> UploadEntity.of(recordEntity, keywordEntity))
                .forEach(recordEntity::addUpload);

        return recordEntity;
    }

    private void addUpload(UploadEntity upload) {
        uploads.add(upload);
        upload.setRecord(this);
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
                .keywords(uploads.stream()
                        .map(UploadEntity::getKeyword)
                        .map(KeywordEntity::toDomain)
                        .toList())
                .uploader(user.toDomain())
                .build();
    }
}
