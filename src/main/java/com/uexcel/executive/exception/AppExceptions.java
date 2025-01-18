package com.uexcel.executive.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class AppExceptions extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int status;
    private final   String description;
    public AppExceptions(int status, String description, String message) {
        super(message);
        this.status = status;
        this.description = description;
    }
}
