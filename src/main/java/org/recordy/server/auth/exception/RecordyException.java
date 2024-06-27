package org.recordy.server.auth.exception;

import lombok.Getter;

@Getter
public class RecordyException extends RuntimeException {
    private final ErrorMessage errorMessage;

    public RecordyException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}
