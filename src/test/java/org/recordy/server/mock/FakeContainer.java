package org.recordy.server.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.recordy.server.auth.repository.AuthRepository;
import org.recordy.server.auth.security.TokenAuthenticationFilter;
import org.recordy.server.auth.security.handler.AuthFilterExceptionHandler;
import org.recordy.server.auth.service.AuthPlatformService;
import org.recordy.server.auth.service.impl.*;
import org.recordy.server.auth.service.AuthService;
import org.recordy.server.auth.service.AuthTokenService;
import org.recordy.server.auth.service.impl.token.AuthTokenGenerator;
import org.recordy.server.auth.service.impl.token.AuthTokenParser;
import org.recordy.server.auth.service.impl.token.AuthTokenServiceImpl;
import org.recordy.server.auth.service.impl.token.AuthTokenSigningKeyProvider;
import org.recordy.server.mock.auth.FakeAuthApplePlatformServiceImpl;
import org.recordy.server.mock.auth.FakeAuthKakaoPlatformServiceImpl;
import org.recordy.server.mock.auth.FakeAuthRepository;
import org.recordy.server.mock.auth.FakeKakaoFeignClient;
import org.recordy.server.mock.record.FakeFileService;
import org.recordy.server.mock.record.FakeRecordRepository;
import org.recordy.server.mock.user.FakeUserRepository;
import org.recordy.server.record.repository.RecordRepository;
import org.recordy.server.record.service.FileService;
import org.recordy.server.record.service.RecordService;
import org.recordy.server.record.service.impl.RecordServiceImpl;
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
    public final FileService fileService;
    public final RecordService recordService;

    // security
    public final AuthFilterExceptionHandler authFilterExceptionHandler;
    public final TokenAuthenticationFilter tokenAuthenticationFilter;

    // controller
    public final UserController userController;

    public FakeContainer() {
        this.userRepository = new FakeUserRepository();
        this.authRepository = new FakeAuthRepository();
        this.recordRepository = new FakeRecordRepository();

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
        this.userService = new UserServiceImpl(userRepository, authService, authTokenService);
        this.fileService = new FakeFileService();
        this.recordService = new RecordServiceImpl(recordRepository, fileService, userService);

        this.authFilterExceptionHandler = new AuthFilterExceptionHandler(new ObjectMapper());
        this.tokenAuthenticationFilter = new TokenAuthenticationFilter(
                DomainFixture.AUTH_FREE_APIS,
                DomainFixture.AUTH_DEV_APIS,
                authTokenService,
                authFilterExceptionHandler
        );

        this.userController = new UserController(userService);
    }
}
