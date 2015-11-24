package com.john.classtools;

public class ClassToolsException extends Exception {
    public ClassToolsException(String message) {
        super(message);
    }

    public ClassToolsException(Throwable cause) { super(cause); }

    public ClassToolsException(String message, Throwable cause) {
        super(message, cause);
    }
}
