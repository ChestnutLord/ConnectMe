package com.dimidev.matchservice.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {
    public NotFoundException(Long id, Class<?> clazz) {
        super(HttpStatus.NOT_FOUND, "Resource not found with id: " + id + " and class: " + clazz.getSimpleName());
    }

    public NotFoundException(String name, Class<?> clazz) {
        super(HttpStatus.NOT_FOUND, "Resource not found with name: " + name + " and class: " + clazz.getSimpleName());
    }
}