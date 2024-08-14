package org.recordy.server.auth.service;

import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.util.DomainFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AuthServiceTest extends FakeContainer {

    @Test
    void create를_통해_Auth_객체를_얻을_수_있다() {
        // given
        User user = DomainFixture.createUser(UserStatus.ACTIVE);
        AuthPlatform platform = DomainFixture.createAuthPlatform();

        // when
        Auth result = authService.create(user, platform);

        // then
        assertAll(
                () -> assertThat(result.getPlatform().getId()).isEqualTo(DomainFixture.PLATFORM_ID),
                () -> assertThat(result.getPlatform().getType()).isEqualTo(platform.getType()),
                () -> assertThat(result.getToken().getAccessToken()).isNotEmpty(),
                () -> assertThat(result.getToken().getRefreshToken()).isNotEmpty(),
                () -> assertThat(result.isSignedUp()).isTrue()
        );
    }

    @Test
    void create를_통해_처음_가입된_사용자의_Auth_객체의_isSignedUp은_false다() {
        // given
        User user = DomainFixture.createUser(UserStatus.PENDING);
        AuthPlatform platform = DomainFixture.createAuthPlatform();

        // when
        Auth result = authService.create(user, platform);

        // then
        assertThat(result.isSignedUp()).isFalse();
    }

    @Test
    void create를_통해_이미_가입된_사용자의_Auth_객체의_isSignedUp은_true다() {
        // given
        User user = DomainFixture.createUser(UserStatus.ACTIVE);
        AuthPlatform platform = DomainFixture.createAuthPlatform();

        // when
        Auth result = authService.create(user, platform);

        // then
        assertThat(result.isSignedUp()).isTrue();
    }

    @Test
    void signOut을_통해_Auth_객체를_삭제할_수_있다() {
        // given
        User user = DomainFixture.createUser(UserStatus.ACTIVE);
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        Auth auth = authService.create(user, platform);

        // when
        authService.signOut(auth.getPlatform().getId());

        // then
        assertThat(authRepository.findByPlatformId(auth.getPlatform().getId())).isEmpty();
    }

    @Test
    void signOut을_통해_존재하지_않는_Auth_객체를_삭제하면_예외가_발생한다() {
        // given
        String platformId = "non-exist-platform-id";

        // when
        // then
        assertThatThrownBy(() -> authService.signOut(platformId))
                .isInstanceOf(AuthException.class)
                .hasMessageContaining(ErrorMessage.AUTH_NOT_FOUND.getMessage());
    }

    @Test
    void getPlatform을_통해_AuthPlatform_객체를_얻을_수_있다() {
        // given
        AuthPlatform.Type platformType = DomainFixture.KAKAO_PLATFORM_TYPE;
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platformType);

        // when
        AuthPlatform result = authService.getPlatform(userSignIn);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(DomainFixture.PLATFORM_ID),
                () -> assertThat(result.getType()).isEqualTo(platformType)
        );
    }
}
