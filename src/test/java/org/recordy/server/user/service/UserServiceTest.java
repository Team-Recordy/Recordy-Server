package org.recordy.server.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.util.DomainFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserServiceTest {

    private FakeContainer fakeContainer;

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        fakeContainer = new FakeContainer();
        userService = fakeContainer.userService;
        userRepository = fakeContainer.userRepository;

        userRepository.save(DomainFixture.createUser());
    }

    @Test
    void create을_통해_플랫폼_정보와_사용자_상태로부터_PENDING_상태의_사용자를_생성할_수_있다() {
        // given
        AuthPlatform authPlatform = DomainFixture.createAuthPlatform();

        // when
        User user = userService.create(authPlatform);

        // then
        assertAll(
                () -> assertThat(user.getAuthPlatform().getId()).isEqualTo(DomainFixture.PLATFORM_ID),
                () -> assertThat(user.getAuthPlatform().getType()).isEqualTo(DomainFixture.KAKAO_PLATFORM_TYPE),
                () -> assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING)
        );
    }

    @Test
    void getByPlatformId를_통해_플랫폼_ID로_사용자_데이터를_조회할_수_있다() {
        // when
        User result = userService.getByPlatformId(DomainFixture.PLATFORM_ID).orElse(null);

        // then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getAuthPlatform().getId()).isEqualTo(DomainFixture.PLATFORM_ID),
                () -> assertThat(result.getAuthPlatform().getType()).isEqualTo(DomainFixture.KAKAO_PLATFORM_TYPE),
                () -> assertThat(result.getStatus()).isEqualTo(DomainFixture.DEFAULT_USER_STATUS)
        );
    }

    @Test
    void getByPlatformId를_통해_존재하지_않는_플랫폼_ID로_사용자_데이터를_조회하면_빈_값을_반환한다() {
        // when
        Optional<User> user = userService.getByPlatformId("non-exist-platform-id");

        // then
        assertThat(user).isEmpty();
    }
}
