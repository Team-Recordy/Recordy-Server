package org.recordy.server.report.controller.exception;

import org.recordy.server.common.exception.RecordyException;
import org.recordy.server.common.message.ErrorMessage;

public class ReportException extends RecordyException {

    public ReportException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}

