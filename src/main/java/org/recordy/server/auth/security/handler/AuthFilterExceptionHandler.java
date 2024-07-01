package org.recordy.server.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.recordy.server.auth.exception.AuthException;
import org.recordy.server.common.dto.response.ErrorResponse;
import org.recordy.server.common.message.ErrorMessage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Component
public class AuthFilterExceptionHandler {

    private final ObjectMapper objectMapper;

    public void handleFilterException(Exception e, HttpServletResponse response) throws IOException {
        if (e instanceof AuthException exception) {
            setErrorResponse(response, exception.getErrorMessage());
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorMessage errorMessage) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(errorMessage.getHttpStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.of(errorMessage)));
    }
}
