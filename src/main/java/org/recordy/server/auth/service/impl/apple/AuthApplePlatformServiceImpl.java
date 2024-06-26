package org.recordy.server.auth.service.impl.apple;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.domain.AuthPlatform.Type;
import org.recordy.server.auth.domain.usecase.AuthSignIn;
import org.recordy.server.auth.service.AuthPlatformService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthApplePlatformServiceImpl implements AuthPlatformService {
    public final AppleOAuthUserProvider appleOAuthUserProvider;

    @Override
    public AuthPlatform getPlatform(AuthSignIn authSignIn) {
        String identityToken = authSignIn.platformToken();
        String id = appleOAuthUserProvider.getApplePlatformId(identityToken);
        return new AuthPlatform(id, Type.APPLE);
    }

    @Override
    public AuthPlatform.Type getPlatformType() {
        return AuthPlatform.Type.APPLE;
    }
}
