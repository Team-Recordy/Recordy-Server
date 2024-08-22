package org.recordy.server.auth.service.impl.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.util.DomainFixture;

import javax.crypto.SecretKey;

import static org.assertj.core.api.Assertions.assertThat;

class AuthTokenSigningKeyProviderTest {

    private AuthTokenSigningKeyProvider signingKeyProvider;

    @BeforeEach
    void setUp() {
        signingKeyProvider = new AuthTokenSigningKeyProvider(DomainFixture.TOKEN_SECRET);
    }

    @Test
    void signingKeyProvider는_HMAC_SHA_256_방식으로_인코딩된_키를_반환한다() {
        // given, when
        SecretKey key = signingKeyProvider.getSigningKey();
        String algorithm = key.getAlgorithm();

        // then
        assertThat(algorithm).isEqualTo("HmacSHA512");
        assertThat(key).isNotNull();
    }
}