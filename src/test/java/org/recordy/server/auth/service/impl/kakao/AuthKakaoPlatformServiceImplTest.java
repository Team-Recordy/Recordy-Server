package org.recordy.server.auth.service.impl.kakao;

import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.domain.usecase.AuthSignIn;
import org.recordy.server.auth.service.AuthPlatformService;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.mock.auth.FakeKakaoFeignClient;
import org.recordy.server.util.DomainFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AuthKakaoPlatformServiceImplTest {

    private AuthPlatformService kakaoPlatformService;

    @BeforeEach
    void init() {
        kakaoPlatformService = new FakeContainer().authKakaoPlatformService;
    }

    @Test
    void getPlatform을_통해_카카오_플랫폼을_통한_사용자_정보를_조회한다() {
        // given
        AuthSignIn authSignIn = DomainFixture.createAuthSignIn(DomainFixture.KAKAO_PLATFORM_TYPE);

        // when
        AuthPlatform platform = kakaoPlatformService.getPlatform(authSignIn);

        // then
        assertAll(
                () -> assertThat(platform.getId()).isEqualTo(DomainFixture.PLATFORM_ID),
                () -> assertThat(platform.getType()).isEqualTo(DomainFixture.KAKAO_PLATFORM_TYPE)
        );
    }

    @Test
    void getPlatform을_통해_카카오_플랫폼에_존재하지_않는_사용자_정보를_조회한_경우_예외를_던진다() {
        // given
        AuthSignIn authSignIn = new AuthSignIn(
                "invalid_token",
                AuthPlatform.Type.KAKAO
        );

        // when, then
        assertThatThrownBy(() -> kakaoPlatformService.getPlatform(authSignIn))
                .isInstanceOf(FeignException.class);
    }
}
