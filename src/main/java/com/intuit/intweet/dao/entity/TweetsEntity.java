package com.intuit.intweet.dao.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "tweets", schema = "intweet", catalog = "")
@IdClass(TweetsEntityPK.class)
public class TweetsEntity implements Serializable {

    @Id
    private int tweetId;
    @Id
    private String employeeId;
    @Column(name = "tweet", nullable = false, length = 500)
    private String tweet;
    @Column(name = "created_datetime", updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDatetime;
    @Column(name = "last_modified_datetime")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDatetime;

    public TweetsEntity() {
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

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Date getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(Date createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public Date getLastModifiedDatetime() {
        return lastModifiedDatetime;
    }

    public void setLastModifiedDatetime(Date lastModifiedDatetime) {
        this.lastModifiedDatetime = lastModifiedDatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TweetsEntity that = (TweetsEntity) o;
        return tweetId == that.tweetId &&
                Objects.equals(employeeId, that.employeeId) &&
                Objects.equals(tweet, that.tweet) &&
                Objects.equals(createdDatetime, that.createdDatetime) &&
                Objects.equals(lastModifiedDatetime, that.lastModifiedDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tweetId, employeeId, tweet, createdDatetime, lastModifiedDatetime);
    }
}
