package org.recordy.server.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.user.controller.dto.request.TermsAgreement;

@AllArgsConstructor
@Builder
@Getter
public class User {

    private Long id;
    private AuthPlatform authPlatform;
    private UserStatus status;
    private String nickname;
    private boolean useTerm;
    private boolean personalInfoTerm;
    private boolean ageTerm;

    public User activate(String nickname, UserStatus status, TermsAgreement termsAgreement) {
        return User.builder()
                .id(this.id)
                .authPlatform(this.authPlatform)
                .nickname(nickname)
                .status(status)
                .useTerm(termsAgreement.useTerm())
                .personalInfoTerm(termsAgreement.personalInfoTerm())
                .ageTerm(termsAgreement.ageTerm())
                .build();
    }
}
