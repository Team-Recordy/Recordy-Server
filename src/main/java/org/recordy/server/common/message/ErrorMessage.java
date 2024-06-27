package org.recordy.server.common.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorMessage {

    ;
    private final HttpStatus httpStatus;
    private final String message;
}
