package org.recordy.server.user.service;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.subscribe.domain.Subscribe;
import org.recordy.server.user.domain.TermsAgreement;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.UserStatus;
import org.recordy.server.user.domain.usecase.UserProfile;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.user.domain.usecase.UserSignUp;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.util.DomainFixture;

import java.util.Optional;

import org.recordy.server.view.domain.View;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserServiceTest extends FakeContainer {

    @Test
    void signIn을_통해_Auth_객체를_얻을_수_있다() {
        // given
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());

        // when
        Auth result = userService.signIn(userSignIn);

        // then
        assertAll(
                () -> assertThat(result.getUserId()).isEqualTo(DomainFixture.USER_ID),
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
        User result = userRepository.findByPlatformId(DomainFixture.PLATFORM_ID);

        // then
        assertAll(
                () -> assertThat(result.getAuthPlatform().getId()).isEqualTo(DomainFixture.PLATFORM_ID),
                () -> assertThat(result.getAuthPlatform().getType()).isEqualTo(platform.getType())
        );
    }

    @Test
    void signIn을_통해_처음_가입된_사용자의_상태는_PENDING이다() {
        // given
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());

        // when
        userService.signIn(userSignIn);
        User user = userRepository.findByPlatformId(DomainFixture.PLATFORM_ID);

        // then
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    @Test
    void signIn을_통해_처음_가입된_사용자의_약관동의_상태는_기본값이다() {
        // given
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());

        // when
        userService.signIn(userSignIn);
        User user = userRepository.findByPlatformId(DomainFixture.PLATFORM_ID);

        // then
        assertThat(user.getTermsAgreement()).isEqualTo(TermsAgreement.defaultAgreement());
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
        assertThatThrownBy(() -> userService.signUp(userSignUp));
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
    void signUp을_통해_사용자의_닉네임과_이미지_약관동의를_추가할_수_있다() {
        // given
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());
        userService.signIn(userSignIn);

        UserSignUp userSignUp = DomainFixture.createUserSignUp();

        // when
        User result = userService.signUp(userSignUp);

        // then
        assertAll(
                () -> assertThat(result.getNickname()).isEqualTo(DomainFixture.USER_NICKNAME),
                () -> assertThat(result.getProfileImageUrl()).contains("https://recordy-bucket.s3.ap-northeast-2.amazonaws.com/profile_"),
                () -> assertThat(result.getTermsAgreement()).isEqualTo(TermsAgreement.of(true, true, true))
        );
    }

    @Test
    void signUp을_통해_사용자를_가입시키면_루트_사용자를_팔로우한다() {
        // given
        Auth rootAuth = userService.signIn(UserSignIn.of("Bearer root", AuthPlatform.Type.KAKAO));
        User rootUser = userService.signUp(UserSignUp.of(rootAuth.getUserId(), "루트", TermsAgreement.of(true, true, true)));

        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());
        Auth auth = userService.signIn(userSignIn);

        // when
        User result = userService.signUp(DomainFixture.createUserSignUp(2L));

        // then
        assertAll(
                () -> assertThat(subscribeRepository.existsBySubscribingUserIdAndSubscribedUserId(result.getId(), DomainFixture.ROOT_USER_ID)).isTrue(),
                () -> assertThat(subscribeRepository.countSubscribingUsers(rootUser.getId())).isEqualTo(1),
                () -> assertThat(subscribeRepository.countSubscribedUsers(result.getId())).isEqualTo(1)
        );
    }

    @Test
    void reissueToken을_통해_accessToken을_재발급_받을_수_있다() {
        //given
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());
        Auth auth = userService.signIn(userSignIn);

        //when
        String refreshToken = DomainFixture.TOKEN_PREFIX + auth.getToken().getRefreshToken();
        String accessToken = userService.reissueToken(refreshToken);

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

        // then
        assertThat(userRepository.findById(DomainFixture.USER_ID)).isNull();
    }

    @Test
    void delete를_통해_사용자를_삭제할_때_관련된_Record_객체도_삭제된다() {
        // given
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());
        Auth auth = userService.signIn(userSignIn);

        // when
        recordRepository.save(DomainFixture.createRecord());
        userService.delete(auth.getUserId());

        // then
        assertAll(
                () -> assertThat(userRepository.findById(DomainFixture.USER_ID)).isNull(),
                () -> assertThat(recordRepository.count()).isZero()
        );
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
    void delete를_통해_사용자를_삭제할_때_관련된_Subscribe_객체도_삭제된다() {
        // given
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());
        userService.signIn(userSignIn);

        // when
        userService.delete(DomainFixture.USER_ID);
        Slice<Subscribe> subscribedresult = subscribeRepository.findAllBySubscribedUserId(DomainFixture.USER_ID, null, Pageable.ofSize(10));
        Slice<Subscribe> SubscribingResult = subscribeRepository.findAllBySubscribingUserId(DomainFixture.USER_ID, null, Pageable.ofSize(10));

        // then
        assertAll(
                () ->  assertThat(subscribedresult).isEmpty(),
                () -> assertThat(SubscribingResult).isEmpty()
        );
    }

    @Test
    void delete를_통해_사용자를_삭제할_때_관련된_Bookmark_객체도_삭제된다() {
        // given
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());
        userService.signIn(userSignIn);

        // when
        userService.delete(DomainFixture.USER_ID);
        Long result = bookmarkRepository.countByUserId(DomainFixture.USER_ID);

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    void delete를_통해_사용자를_삭제할_때_관련된_View_객체도_삭제된다() {
        // given
        AuthPlatform platform = DomainFixture.createAuthPlatform();
        UserSignIn userSignIn = DomainFixture.createUserSignIn(platform.getType());
        userService.signIn(userSignIn);

        // when
        userService.delete(DomainFixture.USER_ID);
        List<View> result = viewRepository.findAllByUserId(DomainFixture.USER_ID);

        // then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void delete를_통해_삭제하고자_하는_사용자가_없을_경우_예외가_발생한다() {
        // given
        long userId = DomainFixture.USER_ID;

        // when, then
        assertThatThrownBy(() -> userService.delete(userId));
    }

    @Test
    void getProfile을_통해_사용자의_프로필_정보를_읽을_수_있다() {
        // given
        User user1 = userRepository.save(DomainFixture.createUser(1));
        User user2 = userRepository.save(DomainFixture.createUser(2));

        recordRepository.save(DomainFixture.createRecord());
        subscribeRepository.save(new Subscribe(1L, user1, user2));
        subscribeRepository.save(new Subscribe(2L, user2, user1));

        // when
        UserProfile userProfile = userService.getProfile(2, DomainFixture.USER_ID);

        // then
        assertAll(
                () -> assertThat(userProfile.id()).isEqualTo(DomainFixture.USER_ID),
                () -> assertThat(userProfile.nickname()).isEqualTo(DomainFixture.USER_NICKNAME),
                () -> assertThat(userProfile.recordCount()).isEqualTo(1),
                () -> assertThat(userProfile.followerCount()).isEqualTo(1),
                () -> assertThat(userProfile.followingCount()).isEqualTo(1)
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
}
