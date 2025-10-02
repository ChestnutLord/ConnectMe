package com.dimidev.connectme.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ApiException extends ResponseStatusException {
    private final HttpStatus status;

    public ApiException(HttpStatus status, String reason) {
        super(status, reason);
        this.status = status;
    }

    public String getStatusMessage() {
        return status.getReasonPhrase();
    }
}
