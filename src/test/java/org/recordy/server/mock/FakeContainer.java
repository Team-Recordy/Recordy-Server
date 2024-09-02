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
import org.recordy.server.exhibition.repository.ExhibitionRepository;
import org.recordy.server.exhibition.service.ExhibitionService;
import org.recordy.server.exhibition.service.impl.ExhibitionServiceImpl;
import org.recordy.server.mock.exhibition.FakeExhibitionRepository;
import org.recordy.server.mock.place.FakePlaceRepository;
import org.recordy.server.mock.record.FakeS3Service;
import org.recordy.server.mock.subscribe.FakeSubscribeRepository;
import org.recordy.server.place.repository.PlaceRepository;
import org.recordy.server.record.service.S3Service;
import org.recordy.server.mock.auth.FakeAuthApplePlatformServiceImpl;
import org.recordy.server.mock.auth.FakeAuthKakaoPlatformServiceImpl;
import org.recordy.server.mock.auth.FakeAuthRepository;
import org.recordy.server.mock.auth.FakeKakaoFeignClient;
import org.recordy.server.mock.bookmark.FakeBookmarkRepository;
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

public class FakeContainer {

    // repository
    public final UserRepository userRepository;
    public final AuthRepository authRepository;
    public final RecordRepository recordRepository;
    public final BookmarkRepository bookmarkRepository;
    public final ViewRepository viewRepository;
    public final SubscribeRepository subscribeRepository;
    public final ExhibitionRepository exhibitionRepository;
    public final PlaceRepository placeRepository;

    // infrastructure
    public final AuthTokenSigningKeyProvider signingKeyProvider;
    public final AuthTokenGenerator tokenGenerator;
    public final AuthTokenParser tokenParser;
    public final FakeKakaoFeignClient fakeKakaoFeignClient;

    // service
    public final AuthPlatformService kakaoPlatformService;
    public final AuthPlatformService applePlatformService;
    public final AuthPlatformServiceFactory platformServiceFactory;
    public final AuthTokenService tokenService;
    public final AuthService authService;
    public final UserService userService;
    public final S3Service s3Service;
    public final RecordService recordService;
    public final BookmarkService bookmarkService;
    public final SubscribeService subscribeService;
    public final ExhibitionService exhibitionService;

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
        this.bookmarkRepository = new FakeBookmarkRepository();
        this.viewRepository = new FakeViewRepository();
        this.subscribeRepository = new FakeSubscribeRepository();
        this.exhibitionRepository = new FakeExhibitionRepository();
        this.placeRepository = new FakePlaceRepository();

        this.signingKeyProvider = new AuthTokenSigningKeyProvider(DomainFixture.TOKEN_SECRET);
        this.tokenGenerator = new AuthTokenGenerator(signingKeyProvider);
        this.tokenParser = new AuthTokenParser(signingKeyProvider);
        this.fakeKakaoFeignClient = new FakeKakaoFeignClient();

        this.kakaoPlatformService = new FakeAuthKakaoPlatformServiceImpl(fakeKakaoFeignClient);
        this.applePlatformService = new FakeAuthApplePlatformServiceImpl();
        this.platformServiceFactory = new AuthPlatformServiceFactory(List.of(kakaoPlatformService, applePlatformService));
        this.tokenService = new AuthTokenServiceImpl(
                DomainFixture.TOKEN_PREFIX,
                DomainFixture.ACCESS_TOKEN_EXPIRATION,
                DomainFixture.REFRESH_TOKEN_EXPIRATION,
                DomainFixture.ACCESS_TOKEN_TYPE,
                DomainFixture.REFRESH_TOKEN_TYPE,
                DomainFixture.USER_ID_KEY,
                DomainFixture.TOKEN_TYPE_KEY,
                tokenGenerator,
                tokenParser,
                authRepository
        );
        this.authService = new AuthServiceImpl(authRepository, platformServiceFactory, tokenService);
        this.userService = new UserServiceImpl(DomainFixture.ROOT_USER_ID, userRepository, subscribeRepository, recordRepository, bookmarkRepository,viewRepository, authService, tokenService);
        this.s3Service = new FakeS3Service();
        this.recordService = new RecordServiceImpl(s3Service, recordRepository, viewRepository, userRepository);
        this.bookmarkService = new BookmarkServiceImpl(userRepository, recordRepository, bookmarkRepository);
        this.subscribeService = new SubscribeServiceImpl(subscribeRepository, userRepository);
        this.exhibitionService = new ExhibitionServiceImpl(exhibitionRepository, placeRepository);

        this.authFilterExceptionHandler = new AuthFilterExceptionHandler(new ObjectMapper());
        this.tokenAuthenticationFilter = new TokenAuthenticationFilter(
                DomainFixture.AUTH_FREE_APIS,
                DomainFixture.AUTH_DEV_APIS,
                tokenService,
                authFilterExceptionHandler
        );

        this.userAuthController = new UserAuthController(userService);
        this.userController = new UserController(subscribeService, userService);
    }
}
