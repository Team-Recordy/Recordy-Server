package org.recordy.server.record.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.user.domain.UserEntity;

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

    public RecordEntity(String videoUrl, String thumbnailUrl, String location, String content, UserEntity user) {
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.location = location;
        this.content = content;
        this.user = user;
    }

    public static RecordEntity from(Record record) {
        return new RecordEntity(
                record.getVideoUrl(),
                record.getThumbnailUrl(),
                record.getLocation(),
                record.getContent(),
                UserEntity.from(record.getUploader())
        );
    }

    public Record toDomain() {
        return new Record(
                videoUrl,
                thumbnailUrl,
                location,
                content,
                user.toDomain()
        );
    }
}
