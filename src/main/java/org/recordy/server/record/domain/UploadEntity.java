package org.recordy.server.record.domain;

import jakarta.persistence.*;
import lombok.*;
import org.recordy.server.record.service.keyword.domain.KeywordEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "uploads")
@Entity
public class UploadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "record_id")
    private RecordEntity record;

    @ManyToOne
    @JoinColumn(name = "keyword_id")
    private KeywordEntity keyword;

    @Builder
    public UploadEntity(Long id, RecordEntity record, KeywordEntity keyword) {
        this.record = record;
        this.keyword = keyword;
    }

    public static UploadEntity of(RecordEntity record, KeywordEntity keyword) {
        return UploadEntity.builder()
                .record(record)
                .keyword(keyword)
                .build();
    }
}
