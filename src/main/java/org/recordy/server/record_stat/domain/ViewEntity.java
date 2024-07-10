package org.recordy.server.record_stat.domain;

import jakarta.persistence.*;
import lombok.*;
import org.recordy.server.common.domain.JpaMetaInfoEntity;
import org.recordy.server.record.domain.RecordEntity;
import org.recordy.server.user.domain.UserEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "views")
@Entity
public class ViewEntity extends JpaMetaInfoEntity {

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
    private ViewEntity(Long id, RecordEntity record, UserEntity user) {
        this.id = id;
        this.record = record;
        this.user = user;
    }

    public static ViewEntity of(RecordEntity record, UserEntity user) {
        ViewEntity view = ViewEntity.builder()
                .record(record)
                .user(user)
                .build();
        record.addView(view);

        return view;
    }
}
