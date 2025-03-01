package org.recordy.server.user.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.common.domain.JpaMetaInfoEntity;
import org.recordy.server.subscribe.domain.SubscribeEntity;

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

    @OneToMany(mappedBy = "subscribingUser", cascade = CascadeType.ALL)
    private Set<SubscribeEntity> subscribings = new HashSet<>();

    @OneToMany(mappedBy = "subscribedUser", cascade = CascadeType.ALL)
    private Set<SubscribeEntity> subscribers = new HashSet<>();

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

    private UserEntity(Long id) {
        this.id = id;
    }

    private UserEntity(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
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

    public static UserEntity of(Long id) {
        return new UserEntity(id);
    }

}
