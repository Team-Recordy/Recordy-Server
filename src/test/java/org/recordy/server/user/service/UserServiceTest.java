package org.recordy.server.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.repository.AuthRepository;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.util.DomainFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserServiceTest {

    private FakeContainer fakeContainer;

    private UserService userService;
    private UserRepository userRepository;
    private AuthRepository authRepository;

    @BeforeEach
    void init() {
        fakeContainer = new FakeContainer();
        userService = fakeContainer.userService;
        userRepository = fakeContainer.userRepository;
        authRepository = fakeContainer.authRepository;
    }

    @Test
    void signIn을_통해_Auth_객체를_얻을_수_있다() {
        // given
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());

        // when
        Auth result = userService.signIn(userSignIn);

        // then
        assertAll(
                () -> assertThat(result.getPlatform().getId()).isEqualTo(DomainFixture.PLATFORM_ID),
                () -> assertThat(result.getPlatform().getType()).isEqualTo(platform.getType()),
                () -> assertThat(result.getToken().getAccessToken()).isNotEmpty(),
                () -> assertThat(result.getToken().getRefreshToken()).isNotEmpty(),
                () -> assertThat(result.isSignedUp()).isFalse()
        );
    }

    @Test
    void signIn을_통해_처음_가입된_사용자의_Auth_객체의_isSignedUp은_false다() {
        // given
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());

        // when
        Auth result = userService.signIn(userSignIn);

        // then
        assertAll(
                () -> assertThat(result.isSignedUp()).isFalse()
        );
    }

    @Test
    void signIn을_통해_처음_가입된_사용자에_대해_User_객체가_저장된다() {
        // given
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());

        // when
        userService.signIn(userSignIn);
        Optional<User> user = userRepository.findByPlatformId(DomainFixture.PLATFORM_ID);

        // then
        assertAll(
                () -> assertThat(user).isNotEmpty(),
                () -> assertThat(user.get().getAuthPlatform().getId()).isEqualTo(DomainFixture.PLATFORM_ID),
                () -> assertThat(user.get().getAuthPlatform().getType()).isEqualTo(platform.getType())
        );
    }

    @Test
    void signIn을_통해_처음_가입된_사용자의_상태는_PENDING이다() {
        // given
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());

        // when
        userService.signIn(userSignIn);
        Optional<User> user = userRepository.findByPlatformId(DomainFixture.PLATFORM_ID);

        // then
        assertAll(
                () -> assertThat(user).isNotEmpty(),
                () -> assertThat(user.get().getStatus()).isEqualTo(UserStatus.PENDING)
        );
    }

    @Test
    void signIn에_넘겨진_이미_가입된_사용자의_상태는_ACTIVE다() {
        // given
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());

        userRepository.save(User.builder()
                .id(DomainFixture.USER_ID)
                .authPlatform(platform)
                .status(UserStatus.ACTIVE)
                .build());

        // when
        Auth result = userService.signIn(userSignIn);

        // then
        assertAll(
                () -> assertThat(result.isSignedUp()).isTrue(),
                () -> assertThat(result.getPlatform().getId()).isEqualTo(DomainFixture.PLATFORM_ID),
                () -> assertThat(result.getPlatform().getType()).isEqualTo(platform.getType())
        );
    }

    @Test
    void validateDuplicateNickname을_통해_중복된_닉네임을_확인할_수_있다() {
        // given
        String nickname = DomainFixture.USER_NICKNAME;
        userRepository.save(User.builder()
                .id(DomainFixture.USER_ID)
                .authPlatform(DomainFixture.createAuthPlatform())
                .status(UserStatus.ACTIVE)
                .nickname(nickname)
                .build());

        // when, then
        assertThatThrownBy(() -> userService.validateDuplicateNickname(nickname))
                .isInstanceOf(UserException.class);
    }

    @Test
    void validateDuplicateNickname을_통해_중복되지_않은_닉네임에_대해서는_아무것도_일어나지_않는다() {
        // given
        String nickname = DomainFixture.USER_NICKNAME;

        // when, then
        assertThatCode(() -> userService.validateDuplicateNickname(nickname))
                .doesNotThrowAnyException();
    }

    @Test
    void delete를_통해_사용자를_삭제할_수_있다() {
        // given
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());
        userService.signIn(userSignIn);

        // when
        userService.delete(DomainFixture.USER_ID);
        Optional<User> result = userRepository.findById(DomainFixture.USER_ID);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void delete를_통해_사용자를_삭제할_때_관련된_Auth_객체도_삭제된다() {
        // given
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());
        userService.signIn(userSignIn);

        // when
        userService.delete(DomainFixture.USER_ID);
        Optional<Auth> result = authRepository.findByPlatformId(DomainFixture.PLATFORM_ID);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void delete를_통해_삭제하고자_하는_사용자가_없을_경우_UserException이_발생한다() {
        // given
        long userId = DomainFixture.USER_ID;

        // when, then
        assertThatThrownBy(() -> userService.delete(userId))
                .isInstanceOf(UserException.class);
    }
}
