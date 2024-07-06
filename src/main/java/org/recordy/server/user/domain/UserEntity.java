package org.recordy.server.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.common.domain.JpaMetaInfoEntity;
import org.recordy.server.user.controller.dto.request.TermsAgreement;

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
    private String nickname;
    private boolean useTerm;
    private boolean personalInfoTerm;
    private boolean ageTerm;

    public UserEntity(Long id, String platformId, AuthPlatform.Type platformType, UserStatus status, String nickname, TermsAgreement termsAgreement) {
        this.id = id;
        this.platformId = platformId;
        this.platformType = platformType;
        this.status = status;
        this.nickname = nickname;
        this.useTerm = termsAgreement.useTerm();
        this.personalInfoTerm = termsAgreement.personalInfoTerm();
        this.ageTerm = termsAgreement.ageTerm();
    }

    public static UserEntity from(User user) {
        return new UserEntity(
                user.getId(),
                user.getAuthPlatform().getId(),
                user.getAuthPlatform().getType(),
                user.getStatus(),
                user.getNickname(),
                user.getTermsAgreement()
        );
    }

    public User toDomain() {
        return new User(
                id,
                new AuthPlatform(platformId, platformType),
                status,
                nickname,
                TermsAgreement.of(useTerm, personalInfoTerm, ageTerm)
        );
    }
}
