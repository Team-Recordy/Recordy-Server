package org.recordy.server.mock;

import org.recordy.server.auth.repository.AuthRepository;
import org.recordy.server.auth.service.AuthPlatformService;
import org.recordy.server.auth.service.AuthPlatformServiceFactory;
import org.recordy.server.auth.service.AuthService;
import org.recordy.server.auth.service.AuthTokenService;
import org.recordy.server.auth.service.impl.apple.AuthApplePlatformServiceImpl;
import org.recordy.server.auth.service.impl.kakao.AuthKakaoPlatformServiceImpl;
import org.recordy.server.auth.service.impl.AuthServiceImpl;
import org.recordy.server.auth.service.impl.AuthTokenServiceImpl;
import org.recordy.server.mock.auth.FakeAuthRepository;
import org.recordy.server.mock.user.FakeUserRepository;
import org.recordy.server.user.controller.UserController;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.user.service.UserService;
import org.recordy.server.user.service.impl.UserServiceImpl;

import java.util.List;

public class FakeContainer {

    // repository
    public final UserRepository userRepository;
    public final AuthRepository authRepository;

    // service
    public final UserService userService;
    public final AuthPlatformService authKakaoPlatformService;
    public final AuthPlatformService authApplePlatformService;
    public final AuthPlatformServiceFactory authPlatformServiceFactory;
    public final AuthTokenService authTokenService;
    public final AuthService authService;

    // controller
    public final UserController userController;

    public FakeContainer() {
        this.userRepository = new FakeUserRepository();
        this.authRepository = new FakeAuthRepository();

        this.userService = new UserServiceImpl(userRepository);
        this.authKakaoPlatformService = new AuthKakaoPlatformServiceImpl();
        this.authApplePlatformService = new AuthApplePlatformServiceImpl();
        this.authPlatformServiceFactory = new AuthPlatformServiceFactory(List.of(authKakaoPlatformService, authApplePlatformService));
        this.authTokenService = new AuthTokenServiceImpl();
        this.authService = new AuthServiceImpl(authRepository, authPlatformServiceFactory, authTokenService, userService);

        this.userController = new UserController(authService);
    }
}
