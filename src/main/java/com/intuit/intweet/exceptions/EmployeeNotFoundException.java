package com.intuit.intweet.exceptions;

public class EmployeeNotFoundException extends Exception {

    public EmployeeNotFoundException(String resourceID, Throwable ex) {
        super(resourceID, ex);
    }

    public EmployeeNotFoundException(String resourceID) {
        super(resourceID);
    }
}
