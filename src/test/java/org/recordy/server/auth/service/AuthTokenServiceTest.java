package org.recordy.server.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.AuthToken;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.util.DomainFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.recordy.server.auth.service.dto.AuthTokenValidationResult.*;

public class AuthTokenServiceTest {

    private FakeContainer fakeContainer;
    private AuthTokenService authTokenService;

    @BeforeEach
    void init() {
        fakeContainer = new FakeContainer();
        authTokenService = fakeContainer.authTokenService;
    }

    @Test
    void userId로부터_AuthToken_객체를_생성해서_반환한다() {
        // given
        long userId = DomainFixture.USER_ID;

        // when
        AuthToken authToken = authTokenService.issueToken(userId);

        // then
        assertAll(
                () -> assertThat(authTokenService.validateToken(authToken.getAccessToken())).isEqualTo(VALID_JWT),
                () -> assertThat(authTokenService.validateToken(authToken.getRefreshToken())).isEqualTo(VALID_JWT)
        );
    }

    @Test
    void 유효하지_않은_토큰을_검증하면_VALID_TOKEN을_반환하지_않는다() {
        // given, when
        String invalidToken = "invalidToken";

        // then
        assertThat(authTokenService.validateToken(invalidToken)).isNotEqualTo(VALID_JWT);
    }
}
