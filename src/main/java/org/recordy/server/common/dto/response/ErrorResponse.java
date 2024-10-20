package org.recordy.server.common.dto.response;

import org.recordy.server.common.message.ErrorMessage;

public record ErrorResponse(
        String errorCode,
        String errorMessage
) {
    public static ErrorResponse of(ErrorMessage errorMessage) {
        return new ErrorResponse(errorMessage.getHttpStatus().toString(), errorMessage.getMessage());
    }

    public static ErrorResponse of(int errorStatus, String errorMessage) {
        return new ErrorResponse(String.valueOf(errorStatus), errorMessage);
    }
}
