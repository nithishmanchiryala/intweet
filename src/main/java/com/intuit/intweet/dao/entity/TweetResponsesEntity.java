package com.intuit.intweet.dao.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "tweet_responses", schema = "intweet", catalog = "")
public class TweetResponsesEntity {
    private int uidpk;
    private int tweetId;
    private String employeeId;
    private String tweetResponse;
    private Timestamp createdDatetime;
    private Timestamp lastModifiedDatetime;

    public TweetResponsesEntity() {

    }

    @Id
    @Column(name = "uidpk", nullable = false)
    public int getUidpk() {
        return uidpk;
    }

    public void setUidpk(int uidpk) {
        this.uidpk = uidpk;
    }

    @Basic
    @Column(name = "tweet_id", nullable = false, length = 20)
    public int getTweetId() {
        return tweetId;
    }

    public void setTweetId(int tweetId) {
        this.tweetId = tweetId;
    }

    @Basic
    @Column(name = "employee_id", nullable = false, length = 20)
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @Basic
    @Column(name = "tweet_response", nullable = false, length = 500)
    public String getTweetResponse() {
        return tweetResponse;
    }

    public void setTweetResponse(String tweetResponse) {
        this.tweetResponse = tweetResponse;
    }

    @Basic
    @Column(name = "created_datetime", nullable = false)
    public Timestamp getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(Timestamp createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    @Basic
    @Column(name = "last_modified_datetime", nullable = false)
    public Timestamp getLastModifiedDatetime() {
        return lastModifiedDatetime;
    }

    public void setLastModifiedDatetime(Timestamp lastModifiedDatetime) {
        this.lastModifiedDatetime = lastModifiedDatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TweetResponsesEntity that = (TweetResponsesEntity) o;
        return uidpk == that.uidpk &&
                tweetId == that.tweetId &&
                Objects.equals(employeeId, that.employeeId) &&
                Objects.equals(tweetResponse, that.tweetResponse) &&
                Objects.equals(createdDatetime, that.createdDatetime) &&
                Objects.equals(lastModifiedDatetime, that.lastModifiedDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uidpk, tweetId, employeeId, tweetResponse, createdDatetime, lastModifiedDatetime);
    }
}
