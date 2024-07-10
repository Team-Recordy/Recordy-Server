package org.recordy.server.util;

import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthEntity;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.domain.AuthToken;
import org.recordy.server.keyword.domain.Keyword;
import org.recordy.server.record.domain.File;
import org.recordy.server.record.domain.Record;
import org.recordy.server.record.domain.RecordEntity;
import org.recordy.server.record.domain.usecase.RecordCreate;
import org.recordy.server.record.service.dto.FileUrl;
import org.recordy.server.record_stat.domain.Bookmark;
import org.recordy.server.user.controller.dto.request.TermsAgreement;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserEntity;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.domain.usecase.UserSignUp;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public final class DomainFixture {

    /**
     * USER
     */
    public static final Long USER_ID = 1L;
    public static final UserStatus DEFAULT_USER_STATUS = UserStatus.ACTIVE;
    public static final String USER_NICKNAME = "레코디";
    public static final boolean USE_TERM_AGREEMENT = true;
    public static final boolean PERSONAL_INFO_TERM_AGREEMENT = true;
    public static final boolean AGE_TERM_AGREEMENT = true;

    /**
     * AUTH
     */
    public static final String PLATFORM_TOKEN = "platform_token";
    public static final String PLATFORM_ID = "abcdefg";
    public static final AuthPlatform.Type KAKAO_PLATFORM_TYPE = AuthPlatform.Type.KAKAO;
    public static final AuthPlatform.Type APPLE_PLATFORM_TYPE = AuthPlatform.Type.APPLE;
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";

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

    /**
     * RECORD
     */
    public static final Long RECORD_ID = 1L;
    public static final String VIDEO_URL = "video_url";
    public static final String THUMBNAIL_URL = "thumbnail_url";
    public static final String LOCATION = "location";
    public static final String CONTENT = "content";

    /**
     * KEYWORD
     */
    public static final Long KEYWORD_ID = 1L;
    public static final Keyword KEYWORD_1 = Keyword.EXOTIC;
    public static final Keyword KEYWORD_2 = Keyword.QUITE;
    public static final Keyword KEYWORD_3 = Keyword.TRENDY;
    public static final List<Keyword> KEYWORDS = List.of(Keyword.EXOTIC, Keyword.QUITE, Keyword.TRENDY);

    public static AuthPlatform createAuthPlatform() {
        return new AuthPlatform(PLATFORM_ID, KAKAO_PLATFORM_TYPE);
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

    public static AuthEntity createAuthEntity(boolean isSignedUp) {
        return new AuthEntity(
                PLATFORM_ID,
                KAKAO_PLATFORM_TYPE.name(),
                ACCESS_TOKEN,
                REFRESH_TOKEN,
                isSignedUp
        );
    }

    public static UserSignIn createUserSignIn(AuthPlatform.Type platformType) {
        return new UserSignIn(
                PLATFORM_TOKEN,
                platformType
        );
    }

    public static UserSignUp createUserSignUp() {
        return new UserSignUp(
                USER_ID,
                USER_NICKNAME,
                TermsAgreement.of(
                        USE_TERM_AGREEMENT,
                        PERSONAL_INFO_TERM_AGREEMENT,
                        AGE_TERM_AGREEMENT
                )
        );
    }

    public static User createUser(UserStatus userStatus) {
        return User.builder()
                .id(USER_ID)
                .authPlatform(createAuthPlatform())
                .status(userStatus)
                .nickname(USER_NICKNAME)
                .termsAgreement(TermsAgreement.of(USE_TERM_AGREEMENT, PERSONAL_INFO_TERM_AGREEMENT, AGE_TERM_AGREEMENT))
                .build();
    }

    public static UserEntity createUserEntity() {
        return new UserEntity(
                USER_ID,
                PLATFORM_ID,
                KAKAO_PLATFORM_TYPE,
                DEFAULT_USER_STATUS,
                USER_NICKNAME,
                TermsAgreement.of(USE_TERM_AGREEMENT, PERSONAL_INFO_TERM_AGREEMENT, AGE_TERM_AGREEMENT),
                LocalDateTime.now()
        );
    }

    public static RecordCreate createRecordCreate() {
        return new RecordCreate(
                USER_ID,
                LOCATION,
                CONTENT,
                KEYWORDS
        );
    }

    public static File createFile() {
        return new File(
                new MockMultipartFile("file", "file.mp4", "video/mp4", new byte[0]),
                new MockMultipartFile("thumbnail", "thumbnail.jpg", "image/jpeg", new byte[0])
        );
    }

    public static Record createRecord() {
        return new Record(
                RECORD_ID,
                new FileUrl(VIDEO_URL, THUMBNAIL_URL),
                LOCATION,
                CONTENT,
                KEYWORDS,
                createUser(UserStatus.ACTIVE)
        );
    }

    public static RecordEntity createRecordEntity() {
        return new RecordEntity(
                RECORD_ID,
                VIDEO_URL,
                THUMBNAIL_URL,
                LOCATION,
                CONTENT,
                createUserEntity()
        );
    }

    public static Bookmark createBookmark() {
        return Bookmark.builder()
                .record(createRecord())
                .user(createUser(UserStatus.ACTIVE))
                .build();
    }
}
