package org.recordy.server.user.domain.usecase;

import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.util.DomainFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserSignInTest {

    @Test
    void of를_통해_AuthSignIn_객체를_생성할_수_있다() {
        // given
        String platformToken = DomainFixture.PLATFORM_TOKEN;
        AuthPlatform.Type platformType = AuthPlatform.Type.KAKAO;

        // when
        UserSignIn userSignIn = UserSignIn.of(platformToken, platformType);

        // then
        assertAll(
                () -> assertThat(userSignIn.platformToken()).isEqualTo(platformToken),
                () -> assertThat(userSignIn.platformType()).isEqualTo(platformType)
        );
    }
}