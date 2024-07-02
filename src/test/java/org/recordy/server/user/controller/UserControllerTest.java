package org.recordy.server.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.user.controller.dto.request.UserSignInRequest;
import org.recordy.server.user.controller.dto.response.UserSignInResponse;
import org.recordy.server.user.domain.User;
import org.recordy.server.user.exception.UserException;
import org.recordy.server.user.repository.UserRepository;
import org.recordy.server.user.service.UserService;
import org.recordy.server.util.DomainFixture;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserControllerTest {

    private FakeContainer fakeContainer;
    private UserController userController;
    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        fakeContainer = new FakeContainer();
        userController = fakeContainer.userController;
        userService = fakeContainer.userService;
        userRepository = fakeContainer.userRepository;
    }

    @Test
    void signIn을_통해_사용자는_카카오_플랫폼_토큰을_통해_가입_이후_토큰을_반환받을_수_있다() {
        // given
        String platformToken = "platform_token";
        UserSignInRequest request = new UserSignInRequest(AuthPlatform.Type.KAKAO);

        // when
        ResponseEntity<UserSignInResponse> result = userController.signIn(platformToken, request);

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
        String platformToken = "platform_token";
        UserSignInRequest request = new UserSignInRequest(AuthPlatform.Type.APPLE);

        // when
        ResponseEntity<UserSignInResponse> result = userController.signIn(platformToken, request);

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
        ResponseEntity<Void> result = userController.checkDuplicateNickname(nickname);

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
        assertThatThrownBy(() -> userController.checkDuplicateNickname(duplicateNickname))
                .isInstanceOf(UserException.class);
    }

    @Test
    void delete를_통해_사용자를_삭제하는_데_성공하면_200_OK를_받는다() {
        // given
        userService.signIn(DomainFixture.createUserSignIn(AuthPlatform.Type.KAKAO));

        // when
        ResponseEntity<Void> result = userController.delete(DomainFixture.USER_ID);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void delete를_통해_존재하지_않는_사용자를_삭제하려고_하면_예외가_발생한다() {
        // when
        assertThatThrownBy(() -> userController.delete(DomainFixture.USER_ID))
                .isInstanceOf(UserException.class);
    }
}
