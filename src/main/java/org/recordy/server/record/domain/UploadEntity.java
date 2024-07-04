package org.recordy.server.record.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.recordy.server.keyword.domain.KeywordEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "uploads")
@Entity
public class UploadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "record_id")
    private RecordEntity record;

    @ManyToOne
    @JoinColumn(name = "keyword_id")
    private KeywordEntity keyword;

    public UploadEntity(RecordEntity record, KeywordEntity keyword) {
        this.record = record;
        this.keyword = keyword;
    }

    public static UploadEntity of(RecordEntity record, KeywordEntity keyword) {
        return new UploadEntity(record, keyword);
    }
}
