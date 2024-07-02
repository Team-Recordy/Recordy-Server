package org.recordy.server.user.exception;


import org.recordy.server.common.exception.RecordyException;
import org.recordy.server.common.message.ErrorMessage;

public class UserException extends RecordyException {

    public UserException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
