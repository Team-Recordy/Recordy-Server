package org.recordy.server.auth.exception;

import org.recordy.server.auth.message.ErrorMessage;
import org.recordy.server.common.exception.RecordyException;

public class UnauthorizedException extends RecordyException {
    public UnauthorizedException() {
        super(ErrorMessage.UNAUTHORIZED);
    }

    public UnauthorizedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
