package com.mishima.tracker.exception;

public class NotLoggedInException extends RuntimeException {

    public NotLoggedInException(String message) {
        super(message);
    }

}
