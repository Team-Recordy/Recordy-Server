package org.recordy.server.record.exception;

import org.recordy.server.common.exception.RecordyException;
import org.recordy.server.common.message.ErrorMessage;

public class RecordException extends RecordyException {

    public RecordException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

}
