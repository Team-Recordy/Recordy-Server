package org.recordy.server.bookmark.exception;

import org.recordy.server.common.exception.RecordyException;
import org.recordy.server.common.message.ErrorMessage;

public class BookmarkException extends RecordyException {

    public BookmarkException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
