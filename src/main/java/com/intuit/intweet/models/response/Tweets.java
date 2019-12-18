package com.intuit.intweet.models.response;

import java.util.ArrayList;
import java.util.List;

public class Tweets {

    private List<Tweet> tweets = new ArrayList<>();

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }
}
