package org.recordy.server.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.common.domain.JpaMetaInfoEntity;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
@Entity
@Setter
public class UserEntity extends JpaMetaInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String platformId;
    @Enumerated(EnumType.STRING)
    private AuthPlatform.Type platformType;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    private String nickname;
    private boolean useTerm;
    private boolean personalInfoTerm;

    public UserEntity(Long id, String platformId, AuthPlatform.Type platformType, UserStatus status, String nickname, boolean useTerm, boolean personalInfoTerm) {
        this.id = id;
        this.platformId = platformId;
        this.platformType = platformType;
        this.status = status;
        this.nickname = nickname;
        this.useTerm = useTerm;
        this.personalInfoTerm = personalInfoTerm;
    }

    public static UserEntity from(User user) {
        return new UserEntity(
                user.getId(),
                user.getAuthPlatform().getId(),
                user.getAuthPlatform().getType(),
                user.getStatus(),
                user.getNickname(),
                user.isUseTerm(),
                user.isPersonalInfoTerm()
        );
    }

    public User toDomain() {
        return new User(
                id,
                new AuthPlatform(platformId, platformType),
                status,
                nickname,
                useTerm,
                personalInfoTerm
        );
    }
}
