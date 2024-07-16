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
import org.recordy.server.bookmark.domain.Bookmark;
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
    public static final Long ROOT_USER_ID = 1L;
    public static final UserStatus DEFAULT_USER_STATUS = UserStatus.ACTIVE;
    public static final String USER_PROFILE_IMAGE_URL = "profile";
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
    public static final Keyword KEYWORD_1 = Keyword.감각적인;
    public static final Keyword KEYWORD_2 = Keyword.강렬한;
    public static final Keyword KEYWORD_3 = Keyword.귀여운;
    public static final List<Keyword> KEYWORDS = List.of(Keyword.감각적인, Keyword.강렬한, Keyword.귀여운);

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
                USER_ID,
                createAuthPlatform(),
                createAuthToken(),
                isSignedUp
        );
    }

    public static AuthEntity createAuthEntity(boolean isSignedUp) {
        return new AuthEntity(
                PLATFORM_ID,
                USER_ID,
                KAKAO_PLATFORM_TYPE.name(),
                ACCESS_TOKEN,
                REFRESH_TOKEN,
                isSignedUp
        );
    }

    public static UserSignIn createUserSignIn(AuthPlatform.Type platformType) {
        return new UserSignIn(
                TOKEN_PREFIX + PLATFORM_TOKEN,
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

    public static UserSignUp createUserSignUp(Long id) {
        return new UserSignUp(
                id,
                USER_NICKNAME,
                TermsAgreement.of(
                        USE_TERM_AGREEMENT,
                        PERSONAL_INFO_TERM_AGREEMENT,
                        AGE_TERM_AGREEMENT
                )
        );
    }

    public static UserSignUp createUserSignUp(Long id, String nickname) {
        return new UserSignUp(
                id,
                nickname,
                TermsAgreement.of(
                        USE_TERM_AGREEMENT,
                        PERSONAL_INFO_TERM_AGREEMENT,
                        AGE_TERM_AGREEMENT
                )
        );
    }

    public static User createUser() {
        return User.builder()
                .id(USER_ID)
                .authPlatform(createAuthPlatform())
                .status(DEFAULT_USER_STATUS)
                .nickname(USER_NICKNAME)
                .termsAgreement(TermsAgreement.of(USE_TERM_AGREEMENT, PERSONAL_INFO_TERM_AGREEMENT, AGE_TERM_AGREEMENT))
                .build();
    }

    public static User createUser(String nickname) {
        return User.builder()
                .id(USER_ID)
                .authPlatform(createAuthPlatform())
                .status(DEFAULT_USER_STATUS)
                .nickname(nickname)
                .termsAgreement(TermsAgreement.of(USE_TERM_AGREEMENT, PERSONAL_INFO_TERM_AGREEMENT, AGE_TERM_AGREEMENT))
                .build();
    }

    public static User createUser(long id) {
        return User.builder()
                .id(id)
                .authPlatform(createAuthPlatform())
                .status(DEFAULT_USER_STATUS)
                .nickname(USER_NICKNAME)
                .termsAgreement(TermsAgreement.of(USE_TERM_AGREEMENT, PERSONAL_INFO_TERM_AGREEMENT, AGE_TERM_AGREEMENT))
                .build();
    }

    public static User createUser(long id, String nickname) {
        return User.builder()
                .id(id)
                .authPlatform(createAuthPlatform())
                .status(DEFAULT_USER_STATUS)
                .nickname(nickname)
                .termsAgreement(TermsAgreement.of(USE_TERM_AGREEMENT, PERSONAL_INFO_TERM_AGREEMENT, AGE_TERM_AGREEMENT))
                .build();
    }

    public static User createUser(UserStatus userStatus) {
        return User.builder()
                .id(USER_ID)
                .authPlatform(createAuthPlatform())
                .status(userStatus)
                .profileImageUrl(USER_PROFILE_IMAGE_URL)
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
                USER_PROFILE_IMAGE_URL,
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
                KEYWORDS,
                new FileUrl(VIDEO_URL, THUMBNAIL_URL)
        );
    }

    public static RecordCreate createRecordCreateByOtherUser() {
        return new RecordCreate(
                2,
                LOCATION,
                CONTENT,
                KEYWORDS,
                new FileUrl(VIDEO_URL, THUMBNAIL_URL)
        );
    }

    public static File createFile() {
        return new File(VIDEO_URL, THUMBNAIL_URL);
    }

    public static Record createRecord() {
        return Record.builder()
                .id(RECORD_ID)
                .fileUrl(new FileUrl(VIDEO_URL, THUMBNAIL_URL))
                .location(LOCATION)
                .content(CONTENT)
                .keywords(KEYWORDS)
                .uploader(createUser(UserStatus.ACTIVE))
                .build();
    }

    public static Record createRecord(long id) {
        return Record.builder()
                .id(id)
                .fileUrl(new FileUrl(VIDEO_URL, THUMBNAIL_URL))
                .location(LOCATION)
                .content(CONTENT)
                .keywords(KEYWORDS)
                .uploader(createUser(UserStatus.ACTIVE))
                .build();
    }

    public static RecordEntity createRecordEntity() {
        return RecordEntity.builder()
                .id(RECORD_ID)
                .videoUrl(VIDEO_URL)
                .thumbnailUrl(THUMBNAIL_URL)
                .location(LOCATION)
                .content(CONTENT)
                .user(createUserEntity())
                .build();
    }

    public static Bookmark createBookmark() {
        return Bookmark.builder()
                .record(createRecord())
                .user(createUser(UserStatus.ACTIVE))
                .build();
    }
}
