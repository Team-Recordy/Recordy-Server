package org.recordy.server.place.exception;

import org.recordy.server.common.exception.RecordyException;
import org.recordy.server.common.message.ErrorMessage;

public class PlaceException extends RecordyException {

    public PlaceException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
