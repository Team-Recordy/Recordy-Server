package org.recordy.server.common.exception;

import lombok.Getter;
import org.recordy.server.common.message.ErrorMessage;

@Getter
public class RecordyException extends RuntimeException {

    private final ErrorMessage errorMessage;

    public RecordyException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}