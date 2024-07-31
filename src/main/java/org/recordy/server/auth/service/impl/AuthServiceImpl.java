package org.recordy.server.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.domain.AuthToken;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.auth.repository.AuthRepository;
import org.recordy.server.auth.service.AuthPlatformService;
import org.recordy.server.auth.service.AuthService;
import org.recordy.server.auth.service.AuthTokenService;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.recordy.server.user.domain.UserStatus.ACTIVE;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final AuthPlatformServiceFactory platformServiceFactory;
    private final AuthTokenService authTokenService;

    @Transactional
    @Override
    public Auth create(User user, AuthPlatform platform) {
        AuthToken token = authTokenService.issueToken(user.getId());

        return authRepository.save(Auth.builder()
                .userId(user.getId())
                .platform(platform)
                .token(token)
                .isSignedUp(user.getStatus().equals(ACTIVE))
                .build());
    }

    @Transactional
    @Override
    public void signOut(String platformId) {
        Auth auth = authRepository.findByPlatformId(platformId)
                .orElseThrow(() -> new AuthException(ErrorMessage.AUTH_NOT_FOUND));

        authRepository.delete(auth);
    }

    @Override
    public AuthPlatform getPlatform(UserSignIn userSignIn) {
        String platformToken = authTokenService.removePrefix(userSignIn.platformToken());
        AuthPlatformService platformService = platformServiceFactory.getPlatformServiceFrom(userSignIn.platformType());

        return platformService.getPlatform(new UserSignIn(platformToken, userSignIn.platformType()));
    }
}
