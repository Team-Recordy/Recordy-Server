package org.recordy.server.auth.service.impl.apple;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.domain.usecase.AuthSignIn;
import org.recordy.server.auth.service.AuthPlatformService;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.util.DomainFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AuthApplePlatformServiceImplTest {

    private FakeContainer fakeContainer;
    private AuthPlatformService applePlatformService;

    @BeforeEach
    void init() {
        fakeContainer = new FakeContainer();
        applePlatformService = fakeContainer.authApplePlatformService;
    }

    @Test
    void getPlatform을_통해_애플_플랫폼을_통한_사용자_정보를_조회한다() {
        // given
        AuthSignIn authSignIn = DomainFixture.createAuthSignIn(DomainFixture.APPLE_PLATFORM_TYPE);

        // when
        AuthPlatform platform = applePlatformService.getPlatform(authSignIn);

        // then
        assertAll(
                () -> assertThat(platform.getId()).isEqualTo(DomainFixture.PLATFORM_ID),
                () -> assertThat(platform.getType()).isEqualTo(DomainFixture.APPLE_PLATFORM_TYPE)
        );
    }
}
