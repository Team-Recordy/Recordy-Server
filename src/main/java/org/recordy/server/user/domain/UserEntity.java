package org.recordy.server.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.common.domain.JpaMetaInfoEntity;
import org.recordy.server.subscribe.domain.SubscribeEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
@Entity
public class UserEntity extends JpaMetaInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String platformId;
    @Enumerated(EnumType.STRING)
    private AuthPlatform.Type platformType;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    private String profileImageUrl;
    private String nickname;
    private boolean useTerm;
    private boolean personalInfoTerm;
    private boolean ageTerm;

    @OneToMany(mappedBy = "subscribingUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubscribeEntity> subscribings = new ArrayList<>();

    @OneToMany(mappedBy = "subscribedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubscribeEntity> subscribers = new ArrayList<>();

    public UserEntity(
            Long id,
            String platformId,
            AuthPlatform.Type platformType,
            UserStatus status,
            String profileImageUrl,
            String nickname,
            TermsAgreement termsAgreement,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.platformId = platformId;
        this.platformType = platformType;
        this.status = status;
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.useTerm = termsAgreement.useTerm();
        this.personalInfoTerm = termsAgreement.personalInfoTerm();
        this.ageTerm = termsAgreement.ageTerm();
        this.createdAt = createdAt;
    }

    public static UserEntity from(User user) {
        return new UserEntity(
                user.getId(),
                user.getAuthPlatform().getId(),
                user.getAuthPlatform().getType(),
                user.getStatus(),
                user.getProfileImageUrl(),
                user.getNickname(),
                user.getTermsAgreement(),
                user.getCreatedAt()
        );
    }
}
