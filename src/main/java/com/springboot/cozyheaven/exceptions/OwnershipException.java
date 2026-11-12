package com.springboot.cozyheaven.exceptions;

public class OwnershipException extends RuntimeException {
    public OwnershipException(String message) {
        super(message);
    }
}