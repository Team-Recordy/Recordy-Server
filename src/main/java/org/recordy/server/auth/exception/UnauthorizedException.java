package org.recordy.server.auth.exception;

public class UnauthorizedException extends RecordyException {
    public UnauthorizedException() {
        super(ErrorMessage.UNAUTHORIZED);
    }

    public UnauthorizedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
