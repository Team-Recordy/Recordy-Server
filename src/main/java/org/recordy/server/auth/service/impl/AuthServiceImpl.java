package org.recordy.server.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.domain.AuthToken;
import org.recordy.server.auth.security.UserAuthentication;
import org.recordy.server.auth.service.impl.token.AuthTokenGenerator;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.auth.repository.AuthRepository;
import org.recordy.server.auth.service.AuthPlatformService;
import org.recordy.server.auth.service.AuthService;
import org.recordy.server.auth.service.AuthTokenService;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.user.domain.User;
import org.springframework.stereotype.Service;

import static org.recordy.server.user.domain.UserStatus.ACTIVE;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final AuthPlatformServiceFactory platformServiceFactory;
    private final AuthTokenService authTokenService;
    @Override
    public Auth create(User user, AuthPlatform platform) {
        AuthToken token = authTokenService.issueToken(user.getId());

        return authRepository.save(Auth.builder()
                .platform(platform)
                .token(token)
                .isSignedUp(user.getStatus().equals(ACTIVE))
                .build());
    }

    @Override
    public void signOut(String platformId) {
        Auth auth = authRepository.findByPlatformId(platformId)
                .orElseThrow(() -> new AuthException(ErrorMessage.AUTH_NOT_FOUND));

        authRepository.delete(auth);
    }

    @Override
    public AuthPlatform getPlatform(UserSignIn userSignIn) {
        AuthPlatformService platformService = platformServiceFactory.getPlatformServiceFrom(userSignIn.platformType());

        return platformService.getPlatform(userSignIn);
    }
}
