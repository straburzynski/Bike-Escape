package com.bikeescape.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserIsRemovedException extends RuntimeException {
    public UserIsRemovedException(String message) {
        super(message);
    }
}
