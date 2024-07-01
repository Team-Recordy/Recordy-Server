package org.recordy.server.auth.exception;

import org.recordy.server.common.message.ErrorMessage;
import org.recordy.server.common.exception.RecordyException;

public class AuthException extends RecordyException {

    public AuthException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
