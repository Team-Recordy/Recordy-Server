package org.recordy.server.auth.service;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.auth.domain.Auth;
import org.recordy.server.auth.domain.AuthPlatform;
import org.recordy.server.auth.domain.AuthToken;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.auth.repository.AuthRepository;
import org.recordy.server.auth.service.dto.AuthTokenValidationResult;
import org.recordy.server.auth.service.impl.token.AuthTokenGenerator;
import org.recordy.server.auth.service.impl.token.AuthTokenSigningKeyProvider;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.mock.FakeContainer;
import org.recordy.server.util.DomainFixture;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.recordy.server.auth.service.dto.AuthTokenValidationResult.*;

public class AuthTokenServiceTest {

    private AuthTokenService authTokenService;
    private AuthTokenSigningKeyProvider signingKeyProvider;
    private AuthTokenGenerator authTokenGenerator;
    private AuthRepository authRepository;

    @BeforeEach
    void init() {
        FakeContainer fakeContainer = new FakeContainer();
        authTokenService = fakeContainer.authTokenService;
        signingKeyProvider = fakeContainer.authTokenSigningKeyProvider;
        authTokenGenerator = fakeContainer.authTokenGenerator;
        authRepository = fakeContainer.authRepository;
    }

    @Test
    void userId로부터_valid한_AuthToken_객체를_생성해서_반환한다() {
        // given
        long userId = DomainFixture.USER_ID;

        // when
        AuthToken authToken = authTokenService.issueToken(userId);

        // then
        assertAll(
                () -> assertThat(authTokenService.validateToken(authToken.getAccessToken())).isEqualTo(VALID_TOKEN),
                () -> assertThat(authTokenService.validateToken(authToken.getRefreshToken())).isEqualTo(VALID_TOKEN)
        );
    }

    @Test
    void 유효하지_않은_토큰을_검증하면_VALID_TOKEN을_반환하지_않는다() {
        // given, when
        String invalidToken = "invalidToken";

        // then
        assertThat(authTokenService.validateToken(invalidToken)).isNotEqualTo(VALID_TOKEN);
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
    void getTokenFromRequest을_통해_HttpRequest에서_토큰을_추출한다() {
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
                .hasMessageContaining(ErrorMessage.INVALID_TOKEN.getMessage());
    }

    @Test
    void HTTP_요청에서_토큰을_파싱한다() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String token = "Bearer abcde";

        request.addHeader(HttpHeaders.AUTHORIZATION, token);

        // when
        String result = authTokenService.getTokenFromRequest(request);

        // then
        assertThat(result).isEqualTo("abcde");
    }

    @Test
    void 알맞은_prefix로_시작하지_않는_토큰에_대해_예외를_던진다() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String token = "abcde";

        request.addHeader(HttpHeaders.AUTHORIZATION, token);

        // when, then
        assertThatThrownBy(() -> authTokenService.getTokenFromRequest(request))
                .isInstanceOf(AuthException.class)
                .hasMessageContaining(ErrorMessage.INVALID_TOKEN.getMessage());
    }

    @Test
    void 토큰에서_사용자_ID를_추출한다() {
        // given
        long userId = DomainFixture.USER_ID;
        AuthToken authToken = authTokenService.issueToken(userId);

        // when
        long result = authTokenService.getUserIdFromToken(authToken.getAccessToken());

        // then
        assertThat(result).isEqualTo(userId);
    }

    @Test
    void 토큰에_사용자_ID가_없을_경우_null_예외를_던진다() {
        // given
        String token = authTokenGenerator.generate(null, 1000L);

        // when, then
        assertThatThrownBy(() -> authTokenService.getUserIdFromToken(token))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void refreshToken으로부터_platformId를_반환한다() {
        //given
        AuthPlatform authPlatform = DomainFixture.createAuthPlatform();
        AuthToken authToken = authTokenService.issueToken(DomainFixture.USER_ID);
        authRepository.save(new Auth(DomainFixture.USER_ID, authPlatform, authToken, true));

        //when
        String refreshToken = "Bearer " + authToken.getRefreshToken();
        String platformId = authTokenService.getPlatformIdFromRefreshToken(refreshToken);

        //then
        assertThat(platformId).isEqualTo(authPlatform.getId());

    }

    @Test
    void 주어진_refreshToken으로부터_platformId를_반환하지_못하면_에러를_던진다() {
        //given
        AuthToken authToken = authTokenService.issueToken(DomainFixture.USER_ID);

        // when, then
        String refreshToken = "Bearer " + authToken.getRefreshToken();
        assertThatThrownBy(() -> authTokenService.getPlatformIdFromRefreshToken(refreshToken))
                .isInstanceOf(AuthException.class)
                .hasMessageContaining(ErrorMessage.AUTH_NOT_FOUND.getMessage());
    }

    @Test
    void issueAccessToken으로_새로운_accessToken을_발급한다() {
        //given
        long userId = DomainFixture.USER_ID;

        //when
        String accessToken = authTokenService.issueAccessToken(userId);

        //then
        assertThat(userId).isEqualTo(authTokenService.getUserIdFromToken(accessToken));
    }
}
