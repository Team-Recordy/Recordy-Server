package org.recordy.server.search.exception;

import org.recordy.server.common.exception.RecordyException;
import org.recordy.server.common.message.ErrorMessage;

public class SearchException extends RecordyException {

    public SearchException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
