package org.recordy.server.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.user.controller.dto.request.UserSignInRequest;
import org.recordy.server.user.controller.dto.response.UserSignInResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserControllerTest {

    private FakeContainer fakeContainer;
    private UserController userController;

    @BeforeEach
    void init() {
        fakeContainer = new FakeContainer();
        userController = fakeContainer.userController;
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
}
