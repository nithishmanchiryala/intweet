package com.intuit.intweet.exceptions;

public class ExceptionThrower {

    public void throwEmployeeNotFoundException() throws CustomException {
        CustomException exception = new CustomException();
        exception.setCode(404);
        exception.setMessage("Employee not found");
        throw exception;
    }

    public void throwTweetNotFoundException() throws CustomException {
        CustomException exception = new CustomException();
        exception.setCode(404);
        exception.setMessage("Tweet not found for the given employeeID and tweetID");
        throw exception;
    }

    public void throwNotFollowingEmployeeException() throws CustomException {
        CustomException exception = new CustomException();
        exception.setCode(400);
        exception.setMessage("Not following or the employee not found");
        throw exception;
    }

    public void throwBadRequestException() throws CustomException {
        CustomException exception = new CustomException();
        exception.setCode(400);
        exception.setMessage("You cannot follow yourself!!");
        throw exception;
    }
}
