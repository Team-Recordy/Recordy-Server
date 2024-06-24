package org.recordy.server.user.controller.dto.response;

import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.util.DomainFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserSignInResponseTest {

    @Test
    void from을_통해_Auth_객체로부터_UserSignInResponse_객체를_생성할_수_있다() {
        // given
        Auth auth = DomainFixture.createAuth(false);

        // when
        UserSignInResponse userSignInResponse = UserSignInResponse.from(auth);

        // then
        assertAll(
                () -> assertThat(userSignInResponse.accessToken()).isEqualTo(auth.getToken().getAccessToken()),
                () -> assertThat(userSignInResponse.refreshToken()).isEqualTo(auth.getToken().getRefreshToken()),
                () -> assertThat(userSignInResponse.isSignedUp()).isEqualTo(auth.isSignedUp())
        );
    }
}