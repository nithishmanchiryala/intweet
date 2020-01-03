package com.intuit.intweet.exceptions;

import org.springframework.stereotype.Component;

@Component
public class CustomException extends Exception {

    private static final long serialVersionUID = -5151732307243953961L;

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
