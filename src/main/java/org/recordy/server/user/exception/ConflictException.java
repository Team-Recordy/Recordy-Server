package org.recordy.server.user.exception;


import org.recordy.server.common.exception.RecordyException;
import org.recordy.server.common.message.ErrorMessage;

public class ConflictException extends RecordyException {

    public ConflictException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
