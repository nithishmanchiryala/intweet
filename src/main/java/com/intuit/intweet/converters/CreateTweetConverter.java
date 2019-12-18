package com.intuit.intweet.converters;

import com.intuit.intweet.dao.entity.TweetsEntity;
import com.intuit.intweet.models.request.CreateTweetRequest;

import org.springframework.core.convert.converter.Converter;

import java.util.Date;

public class CreateTweetConverter implements Converter<CreateTweetRequest, TweetsEntity> {

    @Override
    public TweetsEntity convert(CreateTweetRequest createTweetRequest) {
        TweetsEntity tweetsEntity = new TweetsEntity();
        tweetsEntity.setEmployeeId(createTweetRequest.getEmployeeId());
        tweetsEntity.setTweet(createTweetRequest.getTweet());
        tweetsEntity.setCreatedDatetime(new Date());
        tweetsEntity.setLastModifiedDatetime(new Date());
        return tweetsEntity;
    }
}
