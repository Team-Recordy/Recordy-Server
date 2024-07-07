package org.recordy.server.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.repository.AuthRepository;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.user.controller.dto.request.TermsAgreement;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.user.domain.usecase.UserSignUp;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.util.DomainFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private AuthRepository authRepository;

    @BeforeEach
    void init() {
        FakeContainer fakeContainer = new FakeContainer();
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
    void signUp을_통해_사용자를_발견하지_못하면_예외를_일으킨다() {
        // given
        Long invalidUserId = 99L;
        UserSignUp userSignUp = UserSignUp.of(
                invalidUserId,
                DomainFixture.USER_NICKNAME,
                TermsAgreement.of(true, true, true)
        );

        // when, then
        assertThatThrownBy(() -> userService.signUp(userSignUp))
                .isInstanceOf(UserException.class);
    }

    @Test
    void signUp을_통해_사용자의_상태를_ACTIVE로_변경할_수_있다() {
        // given
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());
        userService.signIn(userSignIn);

        UserSignUp userSignUp = DomainFixture.createUserSignUp();

        // when
        User result = userService.signUp(userSignUp);

        // then
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void reissueToken을_통해_accessToken을_재발급_받을_수_있다() {
        //given
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());
        Auth auth = userService.signIn(userSignIn);

        //when
        String accessToken = userService.reissueToken(auth.getToken().getRefreshToken());

        //then
        assertThat(accessToken).isNotEmpty();

    }

    @Test
    void signOut을_통해_Auth를_삭제할_수_있다() {
        // given
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());
        userService.signIn(userSignIn);

        // when
        userService.signOut(DomainFixture.USER_ID);
        Optional<Auth> result = authRepository.findByPlatformId(DomainFixture.PLATFORM_ID);

        // then
        assertThat(result).isEmpty();
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

    @Test
    void getByPlatformId를_통해_플랫폼_id로부터_사용자를_조회할_수_있다() {
        // given
        userRepository.save(DomainFixture.createUser(UserStatus.ACTIVE));

        // when
        Optional<User> result = userService.getByPlatformId(DomainFixture.PLATFORM_ID);

        // then
        assertAll(
                () -> assertThat(result).isNotEmpty(),
                () -> assertThat(result.get().getAuthPlatform().getId()).isEqualTo(DomainFixture.PLATFORM_ID)
        );
    }

    @Test
    void getByPlatformId를_통해_존재하지_않는_플랫폼_id로_검색할_경우_빈_값을_반환한다() {
        // given
        userRepository.save(DomainFixture.createUser(UserStatus.ACTIVE));

        // when
        Optional<User> result = userService.getByPlatformId("invalid_platform_id");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void getById를_통해_플랫폼_id로부터_사용자를_조회할_수_있다() {
        // given
        userRepository.save(DomainFixture.createUser(UserStatus.ACTIVE));

        // when
        Optional<User> result = userService.getById(DomainFixture.USER_ID);

        // then
        assertAll(
                () -> assertThat(result).isNotEmpty(),
                () -> assertThat(result.get().getId()).isEqualTo(DomainFixture.USER_ID)
        );
    }

    @Test
    void getById를_통해_존재하지_않는_플랫폼_id로_검색할_경우_빈_값을_반환한다() {
        // given
        long invalidUserId = 99L;
        userRepository.save(DomainFixture.createUser(UserStatus.ACTIVE));

        // when
        Optional<User> result = userService.getById(invalidUserId);

        // then
        assertThat(result).isEmpty();
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
}
