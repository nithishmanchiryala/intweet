package com.intuit.intweet.dao.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tweets", schema = "intweet", catalog = "")
public class TweetsEntity {
    private int tweetId;
    private String employeeId;
    private String tweet;
    private Date createdDatetime;
    private Date lastModifiedDatetime;
    private List<TweetResponsesEntity> tweetResponsesByTweetId;

    public TweetsEntity() {
    }

    @Id
    @Column(name = "tweet_id", nullable = false, length = 20)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
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
    @Column(name = "tweet", nullable = false, length = 500)
    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    @Basic
    @Column(name = "created_datetime", nullable = false)
    public Date getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(Date createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    @Basic
    @Column(name = "last_modified_datetime", nullable = false)
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

    @Transient
    @OneToMany(mappedBy = "employees", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<TweetResponsesEntity> getTweetResponsesByTweetId() {
        return tweetResponsesByTweetId;
    }

    public void setTweetResponsesByTweetId(List<TweetResponsesEntity> tweetResponsesByTweetId) {
        this.tweetResponsesByTweetId = tweetResponsesByTweetId;
    }
}
