package org.recordy.server.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.recordy.server.auth.repository.AuthRepository;
import org.recordy.server.auth.security.filter.TokenAuthenticationFilter;
import org.recordy.server.auth.security.handler.AuthFilterExceptionHandler;
import org.recordy.server.auth.service.AuthPlatformService;
import org.recordy.server.auth.service.impl.*;
import org.recordy.server.auth.service.AuthService;
import org.recordy.server.auth.service.AuthTokenService;
import org.recordy.server.auth.service.impl.token.AuthTokenGenerator;
import org.recordy.server.auth.service.impl.token.AuthTokenParser;
import org.recordy.server.auth.service.impl.token.AuthTokenServiceImpl;
import org.recordy.server.auth.service.impl.token.AuthTokenSigningKeyProvider;
import org.recordy.server.bookmark.service.BookmarkService;
import org.recordy.server.bookmark.service.impl.BookmarkServiceImpl;
import org.recordy.server.mock.record.FakeS3Service;
import org.recordy.server.mock.record.FakeUploadRepository;
import org.recordy.server.mock.subscribe.FakeSubscribeRepository;
import org.recordy.server.preference.service.PreferenceService;
import org.recordy.server.preference.service.impl.PreferenceServiceImpl;
import org.recordy.server.record.repository.UploadRepository;
import org.recordy.server.record.service.S3Service;
import org.recordy.server.keyword.repository.KeywordRepository;
import org.recordy.server.keyword.service.KeywordService;
import org.recordy.server.keyword.service.impl.KeywordServiceImpl;
import org.recordy.server.mock.auth.FakeAuthApplePlatformServiceImpl;
import org.recordy.server.mock.auth.FakeAuthKakaoPlatformServiceImpl;
import org.recordy.server.mock.auth.FakeAuthRepository;
import org.recordy.server.mock.auth.FakeKakaoFeignClient;
import org.recordy.server.mock.bookmark.FakeBookmarkRepository;
import org.recordy.server.mock.keyword.FakeKeywordRepository;
import org.recordy.server.mock.record.FakeRecordRepository;
import org.recordy.server.mock.user.FakeUserRepository;
import org.recordy.server.mock.view.FakeViewRepository;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.record.service.RecordService;
import org.recordy.server.record.service.impl.RecordServiceImpl;
import org.recordy.server.bookmark.repository.BookmarkRepository;
import org.recordy.server.view.repository.ViewRepository;
import org.recordy.server.subscribe.repository.SubscribeRepository;
import org.recordy.server.subscribe.service.SubscribeService;
import org.recordy.server.subscribe.service.impl.SubscribeServiceImpl;
import org.recordy.server.user.controller.UserAuthController;
import org.recordy.server.user.controller.UserController;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.user.service.UserService;
import org.recordy.server.user.service.impl.UserServiceImpl;
import org.recordy.server.util.DomainFixture;

import java.util.List;

import static org.mockito.Mockito.mock;

public class FakeContainer {

    // repository
    public final UserRepository userRepository;
    public final AuthRepository authRepository;
    public final RecordRepository recordRepository;
    public final KeywordRepository keywordRepository;
    public final UploadRepository uploadRepository;
    public final BookmarkRepository bookmarkRepository;
    public final ViewRepository viewRepository;
    public final SubscribeRepository subscribeRepository;

    // infrastructure
    public final AuthTokenSigningKeyProvider authTokenSigningKeyProvider;
    public final AuthTokenGenerator authTokenGenerator;
    public final AuthTokenParser authTokenParser;
    public final FakeKakaoFeignClient fakeKakaoFeignClient;

    // service
    public final AuthPlatformService authKakaoPlatformService;
    public final AuthPlatformService authApplePlatformService;
    public final AuthPlatformServiceFactory authPlatformServiceFactory;
    public final AuthTokenService authTokenService;
    public final AuthService authService;
    public final UserService userService;
    public final S3Service s3Service;
    public final RecordService recordService;
    public final KeywordService keywordService;
    public final BookmarkService bookmarkService;
    public final SubscribeService subscribeService;
    public final PreferenceService preferenceService;

    // security
    public final AuthFilterExceptionHandler authFilterExceptionHandler;
    public final TokenAuthenticationFilter tokenAuthenticationFilter;

    // controller
    public final UserAuthController userAuthController;
    public final UserController userController;

    public FakeContainer() {
        this.userRepository = new FakeUserRepository();
        this.authRepository = new FakeAuthRepository();
        this.recordRepository = new FakeRecordRepository();
        this.keywordRepository = new FakeKeywordRepository();
        this.uploadRepository = new FakeUploadRepository();
        this.bookmarkRepository = new FakeBookmarkRepository();
        this.viewRepository = new FakeViewRepository();
        this.subscribeRepository = new FakeSubscribeRepository();

        this.authTokenSigningKeyProvider = new AuthTokenSigningKeyProvider(DomainFixture.TOKEN_SECRET);
        this.authTokenGenerator = new AuthTokenGenerator(authTokenSigningKeyProvider);
        this.authTokenParser = new AuthTokenParser(authTokenSigningKeyProvider);

        this.fakeKakaoFeignClient = new FakeKakaoFeignClient();

        this.authKakaoPlatformService = new FakeAuthKakaoPlatformServiceImpl(fakeKakaoFeignClient);
        this.authApplePlatformService = new FakeAuthApplePlatformServiceImpl();
        this.authPlatformServiceFactory = new AuthPlatformServiceFactory(List.of(authKakaoPlatformService, authApplePlatformService));
        this.authTokenService = new AuthTokenServiceImpl(
                DomainFixture.TOKEN_PREFIX,
                DomainFixture.ACCESS_TOKEN_EXPIRATION,
                DomainFixture.REFRESH_TOKEN_EXPIRATION,
                DomainFixture.ACCESS_TOKEN_TYPE,
                DomainFixture.REFRESH_TOKEN_TYPE,
                DomainFixture.USER_ID_KEY,
                DomainFixture.TOKEN_TYPE_KEY,
                authTokenGenerator,
                authTokenParser,
                authRepository
        );
        this.authService = new AuthServiceImpl(authRepository, authPlatformServiceFactory, authTokenService);
        this.userService = new UserServiceImpl(DomainFixture.ROOT_USER_ID, userRepository, subscribeRepository, recordRepository, bookmarkRepository,viewRepository, authService, authTokenService);

        this.keywordService = new KeywordServiceImpl(keywordRepository);
        this.s3Service = new FakeS3Service();
        this.recordService = new RecordServiceImpl(s3Service, recordRepository, viewRepository, userRepository);
        this.bookmarkService = new BookmarkServiceImpl(userRepository, recordRepository, bookmarkRepository);
        this.subscribeService = new SubscribeServiceImpl(subscribeRepository, userRepository);
        this.preferenceService = new PreferenceServiceImpl(uploadRepository, viewRepository, bookmarkRepository);

        this.authFilterExceptionHandler = new AuthFilterExceptionHandler(new ObjectMapper());
        this.tokenAuthenticationFilter = new TokenAuthenticationFilter(
                DomainFixture.AUTH_FREE_APIS,
                DomainFixture.AUTH_DEV_APIS,
                authTokenService,
                authFilterExceptionHandler
        );

        this.userAuthController = new UserAuthController(userService);
        this.userController = new UserController(subscribeService, userService);
    }
}
