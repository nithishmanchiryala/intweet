package com.intuit.intweet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorMessage> customException(CustomException e) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorCode(e.getCode());
        errorMessage.setErrorMessage(e.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
