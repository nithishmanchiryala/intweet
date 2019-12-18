package com.intuit.intweet.converters;

import com.intuit.intweet.dao.entity.TweetsEntity;
import com.intuit.intweet.dto.TweetsEntityWrapper;
import com.intuit.intweet.models.response.Tweet;
import org.springframework.core.convert.converter.Converter;

public class TweetConverter implements Converter<TweetsEntityWrapper, Tweet> {

    @Override
    public Tweet convert(TweetsEntityWrapper tweetsEntityWrapper) {
        TweetsEntity tweetsEntity = tweetsEntityWrapper.getTweetsEntity();

        Tweet tweet = new Tweet();
        tweet.setTweetId(tweetsEntity.getTweetId());
        tweet.setEmployeeId(tweetsEntity.getEmployeeId());
        tweet.setTweet(tweetsEntity.getTweet());
        return tweet;
    }
}
