package org.recordy.server.external.exception;

import org.recordy.server.common.exception.RecordyException;
import org.recordy.server.common.message.ErrorMessage;

public class ExternalException extends RecordyException {

    public ExternalException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
