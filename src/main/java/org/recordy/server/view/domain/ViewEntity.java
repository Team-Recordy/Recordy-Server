package org.recordy.server.view.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
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
    private ViewEntity(Long id, RecordEntity record, UserEntity user, LocalDateTime createdAt) {
        this.id = id;
        this.record = record;
        this.user = user;
        this.createdAt = createdAt;
    }

    public static ViewEntity of(RecordEntity record, UserEntity user) {
        ViewEntity view = ViewEntity.builder()
                .record(record)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
        record.addView(view);

        return view;
    }

    public static ViewEntity from(View view) {
        ViewEntity viewEntity = ViewEntity.builder()
                .id(view.getId())
                .record(RecordEntity.from(view.getRecord()))
                .user(UserEntity.from(view.getUser()))
                .createdAt(view.getCreatedAt())
                .build();
        viewEntity.getRecord().addView(viewEntity);

        return viewEntity;
    }

    public View toDomain() {
        return View.builder()
                .id(id)
                .record(record.toDomain())
                .user(user.toDomain())
                .createdAt(createdAt)
                .build();
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
