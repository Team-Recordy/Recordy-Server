package org.recordy.server.util;

import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthEntity;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.domain.AuthToken;
import org.recordy.server.auth.domain.usecase.AuthSignIn;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserEntity;
import org.recordy.server.user.domain.UserStatus;

public final class DomainFixture {

    public static final String PLATFORM_TOKEN = "platform_token";
    public static final String PLATFORM_ID = "abcdefg";
    public static final AuthPlatform.Type KAKAO_PLATFORM_TYPE = AuthPlatform.Type.KAKAO;
    public static final AuthPlatform.Type APPLE_PLATFORM_TYPE = AuthPlatform.Type.APPLE;
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final Long USER_ID = 1L;
    public static final UserStatus DEFAULT_USER_STATUS = UserStatus.ACTIVE;
    public static final String USER_NICKNAME = "recordy";

    public static final String TOKEN_SECRET = "secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final long ACCESS_TOKEN_EXPIRATION = 24 * 60 * 60 * 1000L * 14;
    public static final long REFRESH_TOKEN_EXPIRATION = 60 * 60 * 24 * 1000L * 14;
    public static final String ACCESS_TOKEN_TYPE = "access_token";
    public static final String REFRESH_TOKEN_TYPE = "refresh_token";
    public static final String USER_ID_KEY = "user_id";
    public static final String TOKEN_TYPE_KEY = "token_type";

    public static final String[] AUTH_FREE_APIS = new String[]{"/api/v1/users/signIn"};
    public static final String[] AUTH_DEV_APIS = new String[]{"/actuator/health", "/v3/api-docs/**", "/swagger-ui/**", "/docs/**"};

    public static AuthPlatform createAuthPlatform() {
        return new AuthPlatform(PLATFORM_ID, KAKAO_PLATFORM_TYPE);
    }

    public static AuthToken createAuthToken() {
        return new AuthToken(
                ACCESS_TOKEN,
                REFRESH_TOKEN
        );
    }

    public static AuthSignIn createAuthSignIn(AuthPlatform.Type platformType) {
        return new AuthSignIn(
                PLATFORM_TOKEN,
                platformType
        );
    }

    public static Auth createAuth(boolean isSignedUp) {
        return new Auth(
                createAuthPlatform(),
                createAuthToken(),
                isSignedUp
        );
    }

    public static AuthEntity createAuthEntity(boolean isSignedUp) {
        return new AuthEntity(
                PLATFORM_ID,
                KAKAO_PLATFORM_TYPE.name(),
                ACCESS_TOKEN,
                REFRESH_TOKEN,
                isSignedUp
        );
    }

    public static User createUser() {
        return User.builder()
                .id(USER_ID)
                .authPlatform(createAuthPlatform())
                .status(DEFAULT_USER_STATUS)
                .build();
    }

    public static UserEntity createUserEntity() {
        return new UserEntity(
                USER_ID,
                PLATFORM_ID,
                KAKAO_PLATFORM_TYPE,
                DEFAULT_USER_STATUS,
                USER_NICKNAME
        );
    }
}
