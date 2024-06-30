package org.recordy.server.user.exception;

import org.recordy.server.user.message.ErrorMessage;

public class ConflictException extends RuntimeException{
    public ConflictException() {
        super(ErrorMessage.CONFLICT.getMessage());
    }

    public ConflictException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
