package org.recordy.server.slack.exception;

import org.recordy.server.common.exception.RecordyException;
import org.recordy.server.common.message.ErrorMessage;

public class SlackException extends RecordyException {

    public SlackException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
