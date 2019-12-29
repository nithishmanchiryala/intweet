package com.intuit.intweet.dao.entity;

import java.io.Serializable;
import java.util.Objects;

public class TweetsEntityPK implements Serializable {
    private int tweetId;
    private String employeeId;

    public TweetsEntityPK() {

    }

    public int getTweetId() {
        return tweetId;
    }

    public void setTweetId(int tweetId) {
        this.tweetId = tweetId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TweetsEntityPK that = (TweetsEntityPK) o;
        return tweetId == that.tweetId &&
                Objects.equals(employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tweetId, employeeId);
    }
}