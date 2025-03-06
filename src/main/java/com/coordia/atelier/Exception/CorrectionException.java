package com.coordia.atelier.Exception;

public class CorrectionException extends RuntimeException {

    public CorrectionException(String message) {
        super(message);
    }

    public CorrectionException(String message, Throwable cause) {
        super(message, cause);
    }
}