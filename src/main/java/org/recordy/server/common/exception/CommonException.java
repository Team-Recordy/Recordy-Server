package org.recordy.server.common.exception;

import org.recordy.server.common.message.ErrorMessage;

public class CommonException extends RuntimeException{

    private final ErrorMessage errorMessage;
    public CommonException(ErrorMessage errorMessage){
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

}
