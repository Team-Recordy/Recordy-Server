package org.recordy.server.util;

import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.domain.AuthToken;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserEntity;
import org.recordy.server.user.domain.UserStatus;

public final class DomainFixture {

    public static final String PLATFORM_ID = "abcdefg";
    public static final AuthPlatform.Type PLATFORM_TYPE = AuthPlatform.Type.KAKAO;
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final Long USER_ID = 1L;
    public static final UserStatus DEFAULT_USER_STATUS = UserStatus.ACTIVE;

    public static AuthPlatform createAuthPlatform() {
        return new AuthPlatform(PLATFORM_ID, PLATFORM_TYPE);
    }

    public static AuthToken createAuthToken() {
        return new AuthToken(
                ACCESS_TOKEN,
                REFRESH_TOKEN
        );
    }

    public static Auth createAuth(boolean isSignedUp) {
        return new Auth(
                createAuthPlatform(),
                createAuthToken(),
                isSignedUp
        );
    }

    public static UserEntity createUserEntity() {
        return new UserEntity(
                USER_ID,
                PLATFORM_ID,
                PLATFORM_TYPE,
                DEFAULT_USER_STATUS
        );
    }

    public static User createUser() {
        return User.builder()
                .id(USER_ID)
                .authPlatform(createAuthPlatform())
                .status(DEFAULT_USER_STATUS)
                .build();
    }
}
