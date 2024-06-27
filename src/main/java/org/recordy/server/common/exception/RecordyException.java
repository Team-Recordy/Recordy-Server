package org.recordy.server.common.exception;

import org.recordy.server.common.message.ErrorMessage;

public class RecordyException extends RuntimeException{

    private final ErrorMessage errorMessage;
    public RecordyException(ErrorMessage errorMessage){
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

}
