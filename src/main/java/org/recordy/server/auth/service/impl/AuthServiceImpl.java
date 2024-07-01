package org.recordy.server.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.domain.AuthToken;
import org.recordy.server.auth.domain.usecase.AuthSignIn;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.auth.message.ErrorMessage;
import org.recordy.server.auth.repository.AuthRepository;
import org.recordy.server.auth.service.AuthPlatformService;
import org.recordy.server.auth.service.AuthPlatformServiceFactory;
import org.recordy.server.auth.service.AuthService;
import org.recordy.server.auth.service.AuthTokenService;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.service.UserService;
import org.springframework.stereotype.Service;

import static org.recordy.server.user.domain.UserStatus.ACTIVE;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final AuthPlatformServiceFactory platformServiceFactory;
    private final AuthTokenService authTokenService;
    private final UserService userService;

    @Override
    public Auth signIn(AuthSignIn authSignIn) {
        AuthPlatform platform = getPlatform(authSignIn);
        User user = getOrCreateUser(platform);
        AuthToken token = authTokenService.issueToken(user.getId());

        return create(platform, token, user.getStatus());
    }

    private AuthPlatform getPlatform(AuthSignIn authSignIn) {
        AuthPlatformService platformService = platformServiceFactory.getPlatformServiceFrom(authSignIn.platformType());

        return platformService.getPlatform(authSignIn);
    }

    private User getOrCreateUser(AuthPlatform platform) {
        return userService.getByPlatformId(platform.getId())
                .orElseGet(() -> userService.create(platform));
    }

    private Auth create(AuthPlatform platform, AuthToken token, UserStatus userStatus) {
        return authRepository.save(Auth.builder()
                .platform(platform)
                .token(token)
                .isSignedUp(userStatus.equals(ACTIVE))
                .build());
    }

    @Override
    public void signOut(long userId) {
        User user = getUser(userId);
        String platformId = user.getAuthPlatform().getId();
        deleteRefreshToken(platformId);
    }

    private User getUser(Long userId) {
        return userService.getById(userId)
                .orElseThrow(() -> new AuthException(ErrorMessage.USER_NOT_FOUND));
    }

    private void deleteRefreshToken(String platformId) {
        Auth auth = authRepository.findByPlatformId(platformId);
        authRepository.delete(auth);
    }
}
