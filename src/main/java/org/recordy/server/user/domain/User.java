package org.recordy.server.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.recordy.server.auth.domain.AuthPlatform;

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

    public User activate(String nickname, UserStatus status, boolean useTerm, boolean personalInfoTerm, boolean ageTerm) {
        return User.builder()
                .id(this.id)
                .authPlatform(this.authPlatform)
                .nickname(nickname)
                .status(status)
                .useTerm(useTerm)
                .personalInfoTerm(personalInfoTerm)
                .ageTerm(ageTerm)
                .build();
    }
}
