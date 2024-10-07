package org.recordy.server.exhibition.exception;

import org.recordy.server.common.exception.RecordyException;
import org.recordy.server.common.message.ErrorMessage;

public class ExhibitionException extends RecordyException {

    public ExhibitionException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
