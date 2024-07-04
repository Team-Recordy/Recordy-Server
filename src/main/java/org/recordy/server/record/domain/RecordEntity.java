package org.recordy.server.record.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.recordy.server.record.service.dto.FileUrl;
import org.recordy.server.user.domain.UserEntity;

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

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
    private List<UploadEntity> uploads;

    public RecordEntity(String videoUrl, String thumbnailUrl, String location, String content, UserEntity user) {
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.location = location;
        this.content = content;
        this.user = user;
    }

    public static RecordEntity from(Record record) {
        return new RecordEntity(
                record.getFileUrl().videoUrl(),
                record.getFileUrl().thumbnailUrl(),
                record.getLocation(),
                record.getContent(),
                UserEntity.from(record.getUploader())
        );
    }

    public Record toDomain() {
        return new Record(
                id,
                new FileUrl(videoUrl, thumbnailUrl),
                location,
                content,
                uploads.stream()
                        .map(UploadEntity::getKeyword)
                        .map(KeywordEntity::toDomain)
                        .toList(),
                user.toDomain()
        );
    }
}
