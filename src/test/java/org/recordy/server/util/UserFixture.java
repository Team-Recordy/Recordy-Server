package org.recordy.server.util;

import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.user.domain.TermsAgreement;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserStatus;

public class UserFixture {

    public final static AuthPlatform AUTH_PLATFORM = new AuthPlatform("abcdef", AuthPlatform.Type.KAKAO);
    public final static UserStatus USER_STATUS = UserStatus.ACTIVE;
    public final static String NICKNAME = "검정치마";
    public final static TermsAgreement TERMS_AGREEMENT = TermsAgreement.of(true, true, true);

    public static User create() {
        return User.builder()
                .authPlatform(AUTH_PLATFORM)
                .status(USER_STATUS)
                .nickname(NICKNAME)
                .termsAgreement(TERMS_AGREEMENT)
                .build();
    }

    public static User create(String nickname) {
        return User.builder()
                .nickname(nickname)
                .authPlatform(AUTH_PLATFORM)
                .status(USER_STATUS)
                .termsAgreement(TERMS_AGREEMENT)
                .build();
    }
}
