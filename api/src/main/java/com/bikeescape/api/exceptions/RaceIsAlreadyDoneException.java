package com.bikeescape.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RaceIsAlreadyDoneException extends RuntimeException {
    public RaceIsAlreadyDoneException(String message) {
        super(message);
    }
}

