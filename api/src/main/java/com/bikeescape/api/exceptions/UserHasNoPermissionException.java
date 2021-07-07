package com.bikeescape.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserHasNoPermissionException extends RuntimeException {
    public UserHasNoPermissionException(String message) {
        super(message);
    }
}
