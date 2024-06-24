package org.recordy.server.auth.service.impl.apple;

import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.domain.usecase.AuthSignIn;
import org.recordy.server.auth.service.AuthPlatformService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthApplePlatformServiceImpl implements AuthPlatformService {

    @Override
    public AuthPlatform getPlatform(AuthSignIn authSignIn) {
        return null;
    }

    @Override
    public AuthPlatform.Type getPlatformType() {
        return AuthPlatform.Type.APPLE;
    }
}
