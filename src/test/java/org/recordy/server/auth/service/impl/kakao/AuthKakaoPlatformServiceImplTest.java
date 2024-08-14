package org.recordy.server.auth.service.impl.kakao;

import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.util.DomainFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AuthKakaoPlatformServiceImplTest extends FakeContainer {

    @Test
    void getPlatform을_통해_카카오_플랫폼을_통한_사용자_정보를_조회한다() {
        // given
        UserSignIn userSignIn = DomainFixture.createUserSignIn(DomainFixture.KAKAO_PLATFORM_TYPE);
        UserSignIn realUserSignIn = new UserSignIn(
                tokenService.removePrefix(userSignIn.platformToken()),
                DomainFixture.KAKAO_PLATFORM_TYPE
        );

        // when
        AuthPlatform platform = kakaoPlatformService.getPlatform(realUserSignIn);

        // then
        assertAll(
                () -> assertThat(platform.getId()).isEqualTo(DomainFixture.PLATFORM_ID),
                () -> assertThat(platform.getType()).isEqualTo(DomainFixture.KAKAO_PLATFORM_TYPE)
        );
    }

    @Test
    void getPlatform을_통해_카카오_플랫폼에_존재하지_않는_사용자_정보를_조회한_경우_예외를_던진다() {
        // given
        UserSignIn userSignIn = new UserSignIn(
                "invalid_token",
                AuthPlatform.Type.KAKAO
        );

        // when, then
        assertThatThrownBy(() -> kakaoPlatformService.getPlatform(userSignIn))
                .isInstanceOf(FeignException.class);
    }
}
