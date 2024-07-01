package org.recordy.server.auth.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TokenAuthenticationFilterIntegrationTest {

    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    private MockHttpServletRequest request;

    @BeforeEach
    void init() {
        request = new MockHttpServletRequest();
    }

    @ValueSource(strings = {"/api/v1/users/signIn", "/actuator/health", "/v3/api-docs", "/swagger-ui", "/docs"})
    @ParameterizedTest
    void authFreeApis와_authDevApis에_포함된_경로는_필터링되지_않는다(String uri) throws Exception {
        // given
        request.setRequestURI(uri);

        // when
        boolean isNotFilter = tokenAuthenticationFilter.shouldNotFilter(request);

        // then
        assertThat(isNotFilter).isTrue();
    }

    @Test
    void authFreeApis와_authDevApis에_포함되지_않은_경로는_필터링된다() throws Exception {
        // given
        request.setRequestURI("/api/v1/users");

        // when
        boolean isNotFilter = tokenAuthenticationFilter.shouldNotFilter(request);

        // then
        assertThat(isNotFilter).isFalse();
    }
}