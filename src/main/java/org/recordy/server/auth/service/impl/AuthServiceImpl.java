package org.recordy.server.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.domain.AuthToken;
import org.recordy.server.auth.domain.usecase.AuthSignIn;
import org.recordy.server.auth.repository.AuthRepository;
import org.recordy.server.auth.service.AuthPlatformService;
import org.recordy.server.auth.service.AuthService;
import org.recordy.server.auth.service.AuthTokenService;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.service.UserService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final AuthPlatformService platformService;
    private final AuthTokenService authTokenService;
    private final UserService userService;

    @Override
    public Auth signIn(AuthSignIn authSignIn) {
        AuthPlatform platform = platformService.getPlatform(authSignIn);
        User user = getOrCreateUser(platform);
        AuthToken token = authTokenService.issueToken(user.getId());

        return create(platform, token);
    }

    private User getOrCreateUser(AuthPlatform platform) {
        return userService.getByPlatformId(platform.getId())
                .orElseGet(() -> userService.create(platform, UserStatus.PENDING));
    }

    private Auth create(AuthPlatform platform, AuthToken token) {
        return authRepository.save(Auth.builder()
                .platform(platform)
                .token(token)
                .build());
    }
}
