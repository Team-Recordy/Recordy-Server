package org.recordy.server.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recordy.server.common.message.ErrorMessage;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class UndefinedAccessHandlerTest {

    private UndefinedAccessHandler undefinedAccessHandler;

    @BeforeEach
    void init() {
        undefinedAccessHandler = new UndefinedAccessHandler(new ObjectMapper());
    }

    @Test
    void NOT_FOUND_응답을_반환한다() throws Exception {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        undefinedAccessHandler.handle(null, response, null);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_NOT_FOUND);
        assertThat(response.getContentAsString()).contains(ErrorMessage.RESOURCE_NOT_FOUND.getMessage());
    }
}