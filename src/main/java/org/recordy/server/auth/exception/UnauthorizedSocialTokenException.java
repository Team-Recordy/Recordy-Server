package org.recordy.server.auth.exception;

import org.recordy.server.auth.message.ErrorMessage;

public class UnauthorizedSocialTokenException extends RuntimeException {
    public UnauthorizedSocialTokenException() {
        super(ErrorMessage.UNAUTHORIZED.getMessage());
    }

    public UnauthorizedSocialTokenException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
