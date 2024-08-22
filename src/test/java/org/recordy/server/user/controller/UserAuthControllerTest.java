package org.recordy.server.user.controller;

import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.user.controller.dto.request.UserSignInRequest;
import org.recordy.server.user.controller.dto.response.UserReissueTokenResponse;
import org.recordy.server.user.controller.dto.response.UserSignInResponse;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.domain.usecase.UserSignIn;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.util.DomainFixture;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserAuthControllerTest extends FakeContainer {

    @Test
    void signIn을_통해_사용자는_카카오_플랫폼_토큰을_통해_가입_이후_토큰을_반환받을_수_있다() {
        // given
        UserSignIn userSignIn = DomainFixture.createUserSignIn(AuthPlatform.Type.KAKAO);
        UserSignInRequest userSignInRequest = new UserSignInRequest(AuthPlatform.Type.KAKAO);

        // when
        ResponseEntity<UserSignInResponse> result = userAuthController.signIn(userSignIn.platformToken(), userSignInRequest);

        // then
        assertAll(
                () -> assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(result.getBody().accessToken()).isNotNull(),
                () -> assertThat(result.getBody().refreshToken()).isNotNull(),
                () -> assertThat(result.getBody().isSignedUp()).isFalse()
        );
    }

    @Test
    void signIn을_통해_사용자는_애플_플랫폼_토큰을_통해_가입_이후_토큰을_반환받을_수_있다() {
        // given
        UserSignIn userSignIn = DomainFixture.createUserSignIn(AuthPlatform.Type.APPLE);
        UserSignInRequest userSignInRequest = new UserSignInRequest(AuthPlatform.Type.APPLE);

        // when
        ResponseEntity<UserSignInResponse> result = userAuthController.signIn(userSignIn.platformToken(), userSignInRequest);

        // then
        assertAll(
                () -> assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(result.getBody().accessToken()).isNotNull(),
                () -> assertThat(result.getBody().refreshToken()).isNotNull(),
                () -> assertThat(result.getBody().isSignedUp()).isFalse()
        );
    }

    @Test
    void checkDuplicateNickname을_통해_중복된_닉네임이_없는_경우_200_OK를_받는다() {
        // given
        String nickname = "nickname";

        // when
        ResponseEntity<Void> result = userAuthController.checkDuplicateNickname(nickname);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void checkDuplicateNickname을_통해_중복된_닉네임이_있는_경우_예외가_발생한다() {
        // given
        String duplicateNickname = "nickname";

        userRepository.save(User.builder()
                .id(DomainFixture.USER_ID)
                .authPlatform(DomainFixture.createAuthPlatform())
                .nickname(duplicateNickname)
                .build());

        // when
        assertThatThrownBy(() -> userAuthController.checkDuplicateNickname(duplicateNickname))
                .isInstanceOf(UserException.class);
    }

    @Test
    void reissueToken을_통해_accessToken을_재발급_받을_수_있다() {
        //given
        Auth auth = userService.signIn(DomainFixture.createUserSignIn(AuthPlatform.Type.KAKAO));
        String refreshToken = "Bearer " + auth.getToken().getRefreshToken();


        //when
        ResponseEntity<UserReissueTokenResponse> result = userAuthController.reissueToken(refreshToken);

        //then
        assertAll(
                () -> assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(result.getBody().accessToken()).isNotNull()
        );
    }

    @Test
    void reissueToken에서_refreshToken으로_인증정보를_찾을_수_없다면_에러가_발생한다() {

        //when
        assertThatThrownBy(() -> userAuthController.reissueToken(DomainFixture.REFRESH_TOKEN))
                .isInstanceOf(AuthException.class);
    }

    @Test
    void signOut을_통해_사용자를_로그아웃하는_데_성공하면_204_No_Content를_받는다() {
        //given
        userService.signIn(DomainFixture.createUserSignIn(AuthPlatform.Type.KAKAO));

        //when
        ResponseEntity result = userAuthController.signOut(DomainFixture.USER_ID);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void signOut을_통해_존재하지_않는_사용자를_로그아웃하려고_하면_예외가_발생한다() {
        //when, then
        assertThatThrownBy(() -> userAuthController.signOut(DomainFixture.USER_ID));
    }

    @Test
    void delete를_통해_사용자를_삭제하는_데_성공하면_204_NO_CONTENT를_받는다() {
        // given
        userService.signIn(DomainFixture.createUserSignIn(AuthPlatform.Type.KAKAO));

        // when
        ResponseEntity<Void> result = userAuthController.delete(DomainFixture.USER_ID);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void delete를_통해_존재하지_않는_사용자를_삭제하려고_하면_예외가_발생한다() {
        // when
        assertThatThrownBy(() -> userAuthController.delete(DomainFixture.USER_ID));
    }
}
