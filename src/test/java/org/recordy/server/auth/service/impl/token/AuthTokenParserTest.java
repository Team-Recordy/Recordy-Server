package org.recordy.server.auth.service.impl.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.mock.FakeContainer;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AuthTokenParserTest {

    private AuthTokenParser authTokenParser;
    private AuthTokenGenerator authTokenGenerator;

    @BeforeEach
    void init() {
        FakeContainer fakeContainer = new FakeContainer();
        authTokenParser = fakeContainer.authTokenParser;
        authTokenGenerator = fakeContainer.authTokenGenerator;
    }

    @Test
    void getBody를_통해_토큰의_내용을_읽을_수_있다() {
        // given
        String token = authTokenGenerator.generate(Map.of("A", "a", "B", "b"), 10000000L);

        // when
        Claims body = authTokenParser.getBody(token);

        // then
        assertThat(body.get("A")).isEqualTo("a");
        assertThat(body.get("B")).isEqualTo("b");
        assertThat(body.getExpiration().getTime() - body.getIssuedAt().getTime()).isEqualTo(10000000L);
    }

    @Test
    void 잘못된_토큰에_대해_JWT에서_지원하는_예외를_발생시킨다() {
        // given
        String token = "wrong token";

        // when, then
        Assertions.assertThatThrownBy(() -> authTokenParser.getBody(token))
                .isInstanceOf(JwtException.class);
    }
}