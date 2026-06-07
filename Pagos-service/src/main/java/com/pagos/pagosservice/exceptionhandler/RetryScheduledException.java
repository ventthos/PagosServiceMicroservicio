package com.pagos.pagosservice.exceptionhandler;

public class RetryScheduledException extends RuntimeException {
    public RetryScheduledException(String message) {
        super(message);
    }
}
