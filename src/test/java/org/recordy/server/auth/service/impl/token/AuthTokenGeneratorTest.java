package org.recordy.server.auth.service.impl.token;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.util.DomainFixture;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AuthTokenGeneratorTest {

    private AuthTokenGenerator tokenGenerator;
    private AuthTokenParser tokenParser;

    @BeforeEach
    void setUp() {
        AuthTokenSigningKeyProvider signingKeyProvider = new AuthTokenSigningKeyProvider(DomainFixture.TOKEN_SECRET);
        tokenGenerator = new AuthTokenGenerator(signingKeyProvider);
        tokenParser = new AuthTokenParser(signingKeyProvider);
    }

    @Test
    void generate를_통해_토큰을_생성할_수_있다() {
        // given
        String token = tokenGenerator.generate(Map.of("A", "a", "B", "b"), 100000L);

        // when
        Claims body = tokenParser.getBody(token);

        // then
        assertThat(body.get("A")).isEqualTo("a");
        assertThat(body.get("B")).isEqualTo("b");
        assertThat(body.getExpiration().getTime() - body.getIssuedAt().getTime()).isEqualTo(100000L);
    }
}