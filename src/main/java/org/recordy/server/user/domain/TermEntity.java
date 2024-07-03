package org.recordy.server.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.recordy.server.common.domain.JpaMetaInfoEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "terms")
@Entity
public class TermEntity extends JpaMetaInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private boolean check;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public TermEntity(String content, boolean check, UserEntity user) {
        this.content = content;
        this.check = check;
        this.user = user;
    }
}

