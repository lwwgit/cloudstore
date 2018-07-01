package com.example.cloudstore.exception;


import org.springframework.security.core.AuthenticationException;

public class ValidateCodeException extends AuthenticationException {
    private String message;

//    public ValidateCodeException(String msg, Throwable t) {
//        super(msg, t);
//    }

    public ValidateCodeException(String msg) {
        super(msg);
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
