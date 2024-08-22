package org.recordy.server.auth.security.filter;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.mock.FakeContainer;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class TokenAuthenticationFilterTest extends FakeContainer {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void init() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
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

    @Test
    void prefix_형식만_맞는_잘못된_토큰을_필터링하면_예외가_발생하고_UNAUTHORIZED_응답을_반환한다() throws Exception {
        // given
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer abcdfe");

        // when
        tokenAuthenticationFilter.doFilterInternal(request, response, new MockFilterChain());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(response.getContentAsString()).contains(ErrorMessage.INVALID_TOKEN_VALUE.getMessage());
    }
}