package com.intuit.intweet.dto;

import com.intuit.intweet.dao.entity.TweetsEntity;

public class TweetsEntityWrapper {

    private TweetsEntity tweetsEntity;

    public TweetsEntityWrapper() {

    }

    public TweetsEntityWrapper(TweetsEntity tweetsEntity) {
        this.tweetsEntity = tweetsEntity;
    }

    public TweetsEntity getTweetsEntity() {
        return tweetsEntity;
    }

    public void setTweetsEntity(TweetsEntity tweetsEntity) {
        this.tweetsEntity = tweetsEntity;
    }
}
