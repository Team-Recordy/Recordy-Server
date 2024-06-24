package org.recordy.server.auth.domain.usecase;

import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.util.DomainFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AuthSignInTest {

    @Test
    void of를_통해_AuthSignIn_객체를_생성할_수_있다() {
        // given
        String platformToken = DomainFixture.PLATFORM_TOKEN;
        AuthPlatform.Type platformType = AuthPlatform.Type.KAKAO;

        // when
        AuthSignIn authSignIn = AuthSignIn.of(platformToken, platformType);

        // then
        assertAll(
                () -> assertThat(authSignIn.platformToken()).isEqualTo(platformToken),
                () -> assertThat(authSignIn.platformType()).isEqualTo(platformType)
        );
    }
}