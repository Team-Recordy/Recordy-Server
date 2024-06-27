package org.recordy.server.auth.exception;

import org.recordy.server.auth.message.ErrorMessage;
import org.recordy.server.common.exception.RecordyException;

public class UnauthorizedSocialTokenException extends RecordyException {
    public UnauthorizedSocialTokenException() {
        super(ErrorMessage.UNAUTHORIZED);
    }

    public UnauthorizedSocialTokenException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
