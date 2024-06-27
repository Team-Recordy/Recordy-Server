package org.recordy.server.auth.exception;

import org.recordy.server.auth.message.ErrorMessage;

public class AuthException extends RuntimeException {

    public AuthException() {
        super(ErrorMessage.UNAUTHORIZED.getMessage());
    }

    public AuthException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
