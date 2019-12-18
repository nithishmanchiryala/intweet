package com.intuit.intweet.models.request;

import javax.validation.constraints.NotEmpty;

public class CreateTweetRequest {

    @NotEmpty(message = "Please provide employeeId")
    private String employeeId;
    @NotEmpty(message = "Please provide tweet")
    private String tweet;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }
}
