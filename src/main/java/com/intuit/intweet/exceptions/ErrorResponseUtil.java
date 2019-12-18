package com.intuit.intweet.exceptions;

public class ErrorResponseUtil {

    public static void throwEmployeeNotFoundException(String resourceID) throws EmployeeNotFoundException {
        throw new EmployeeNotFoundException(resourceID);
    }
}
