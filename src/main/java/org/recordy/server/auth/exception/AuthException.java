package org.recordy.server.auth.exception;

import org.recordy.server.auth.message.ErrorMessage;

public class AuthException extends RuntimeException {

    private final ErrorMessage errorMessage;
    public AuthException(ErrorMessage errorMessage){
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

}
