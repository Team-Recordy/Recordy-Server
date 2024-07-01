package org.recordy.server.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.recordy.server.mock.user.FakeUserRepository;
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

    // infrastructure
    public final AuthTokenSigningKeyProvider authTokenSigningKeyProvider;
    public final AuthTokenGenerator authTokenGenerator;
    public final AuthTokenParser authTokenParser;

    // service
    public final UserService userService;
    public final AuthPlatformService authKakaoPlatformService;
    public final AuthPlatformService authApplePlatformService;
    public final AuthPlatformServiceFactory authPlatformServiceFactory;
    public final AuthTokenService authTokenService;
    public final AuthService authService;

    // security
    public final AuthFilterExceptionHandler authFilterExceptionHandler;
    public final TokenAuthenticationFilter tokenAuthenticationFilter;

    // controller
    public final UserController userController;

    public FakeContainer() {
        this.userRepository = new FakeUserRepository();
        this.authRepository = new FakeAuthRepository();

        this.authTokenSigningKeyProvider = new AuthTokenSigningKeyProvider(DomainFixture.TOKEN_SECRET);
        this.authTokenGenerator = new AuthTokenGenerator(authTokenSigningKeyProvider);
        this.authTokenParser = new AuthTokenParser(authTokenSigningKeyProvider);

        this.userService = new UserServiceImpl(userRepository);
        this.authKakaoPlatformService = new FakeAuthKakaoPlatformServiceImpl();
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
                authTokenParser
        );
        this.authService = new AuthServiceImpl(authRepository, authPlatformServiceFactory, authTokenService, userService);

        this.authFilterExceptionHandler = new AuthFilterExceptionHandler(new ObjectMapper());
        this.tokenAuthenticationFilter = new TokenAuthenticationFilter(
                DomainFixture.AUTH_FREE_APIS,
                DomainFixture.AUTH_DEV_APIS,
                authTokenService,
                authFilterExceptionHandler
        );

        this.userController = new UserController(authService);
    }
}
