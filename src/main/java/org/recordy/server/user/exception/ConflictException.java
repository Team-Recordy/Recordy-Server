package org.recordy.server.user.exception;

public class ConflictException extends RuntimeException{
    public ConflictException() {
        super(ErrorMessage.CONFLICT);
    }

    public ConflictException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
