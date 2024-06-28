package org.recordy.server.auth.service;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.AuthToken;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.auth.message.ErrorMessage;
import org.recordy.server.auth.service.dto.AuthTokenValidationResult;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.util.DomainFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.recordy.server.auth.service.dto.AuthTokenValidationResult.*;

@SpringBootTest
public class AuthTokenServiceIntegrationTest {

    @Autowired
    private AuthTokenService authTokenService;

    @Test
    void userId로부터_valid한_AuthToken_객체를_생성해서_반환한다() {
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

    @Test
    void 만료된_토큰을_검증하면_EXPIRED_TOKEN을_반환한다() {
        // given
        Date expiredDate = new Date(System.currentTimeMillis() - 1000);

        String expiredToken = Jwts.builder()
                .setSubject(String.valueOf(DomainFixture.USER_ID))
                .setExpiration(expiredDate)
                .compact();

        // when
        AuthTokenValidationResult result = authTokenService.validateToken(expiredToken);

        // then
        assertThat(result).isEqualTo(EXPIRED_TOKEN);
    }

    @Test
    void 비어있는_토큰을_검증하면_EMPTY_TOKEN을_반환한다() {
        // given
        String emptyToken = "";

        // when
        AuthTokenValidationResult result = authTokenService.validateToken(emptyToken);

        // then
        assertThat(result).isEqualTo(EMPTY_TOKEN);
    }

    @Test
    void 토큰을_파싱하여_prefix를_제외하고_반환한다() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String token = "Bearer token";
        request.addHeader(HttpHeaders.AUTHORIZATION, token);

        // when
        String result = authTokenService.getTokenFromRequest(request);

        // then
        assertThat(result).isEqualTo("token");
    }

    @Test
    void prefix가_잘못된_토큰을_파싱하면_예외를_던진다() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String token = "token";
        request.addHeader(HttpHeaders.AUTHORIZATION, token);

        // when, then
        assertThatThrownBy(() -> authTokenService.getTokenFromRequest(request))
                .isInstanceOf(AuthException.class)
                .hasMessageContaining(ErrorMessage.INVALID_ACCESS_TOKEN.getMessage());
    }
}
