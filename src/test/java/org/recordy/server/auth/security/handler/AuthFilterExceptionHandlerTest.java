package org.recordy.server.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.common.message.ErrorMessage;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class AuthFilterExceptionHandlerTest {

    AuthFilterExceptionHandler authFilterExceptionHandler;

    @BeforeEach
    void setUp() {
        authFilterExceptionHandler = new AuthFilterExceptionHandler(new ObjectMapper());
    }

    @Test
    void 예외에_담긴_에러_메시지를_기반으로_Http_응답을_생성한다() throws Exception {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();
        ErrorMessage errorMessage = ErrorMessage.EXPIRED_TOKEN;
        AuthException authException = new AuthException(errorMessage);

        // when
        authFilterExceptionHandler.handleFilterException(authException, response);

        // then
        assertThat(response.getStatus()).isEqualTo(errorMessage.getHttpStatus().value());
        assertThat(response.getContentAsString()).contains(errorMessage.getMessage());
    }
}