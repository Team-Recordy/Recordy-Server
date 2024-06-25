package org.recordy.server.mock.auth;

import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.domain.usecase.AuthSignIn;
import org.recordy.server.auth.service.AuthPlatformService;
import org.recordy.server.util.DomainFixture;

public class FakeAuthKakaoPlatformServiceImpl implements AuthPlatformService {

    @Override
    public AuthPlatform getPlatform(AuthSignIn authSignIn) {
        return new AuthPlatform(
                DomainFixture.PLATFORM_ID,
                AuthPlatform.Type.KAKAO
        );
    }

    @Override
    public AuthPlatform.Type getPlatformType() {
        return AuthPlatform.Type.KAKAO;
    }
}
