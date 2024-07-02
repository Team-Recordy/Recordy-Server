package org.recordy.server.auth.service.impl.token;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.mock.FakeContainer;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AuthTokenGeneratorTest {

    private AuthTokenGenerator authTokenGenerator;
    private AuthTokenParser authTokenParser;

    @BeforeEach
    void init() {
        FakeContainer fakeContainer = new FakeContainer();
        authTokenGenerator = fakeContainer.authTokenGenerator;
        authTokenParser = fakeContainer.authTokenParser;
    }

    @Test
    void generate를_통해_토큰을_생성할_수_있다() {
        // given
        String token = authTokenGenerator.generate(Map.of("A", "a", "B", "b"), 100000L);

        // when
        Claims body = authTokenParser.getBody(token);

        // then
        assertThat(body.get("A")).isEqualTo("a");
        assertThat(body.get("B")).isEqualTo("b");
        assertThat(body.getExpiration().getTime() - body.getIssuedAt().getTime()).isEqualTo(100000L);
    }
}