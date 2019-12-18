package com.intuit.intweet.services.impl;

import com.intuit.intweet.dao.entity.EmployeesEntity;
import com.intuit.intweet.dao.entity.FollowersEntity;
import com.intuit.intweet.dao.entity.FollowersEntityPK;
import com.intuit.intweet.dao.entity.TweetsEntity;
import com.intuit.intweet.dao.service.IntweetDaoService;
import com.intuit.intweet.dto.TweetsEntityWrapper;
import com.intuit.intweet.exceptions.EmployeeNotFoundException;
import com.intuit.intweet.exceptions.ErrorResponseUtil;
import com.intuit.intweet.models.request.CreateTweetRequest;
import com.intuit.intweet.models.response.Follower;
import com.intuit.intweet.models.response.Tweet;
import com.intuit.intweet.models.response.Tweets;
import com.intuit.intweet.services.IntweetService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IntweetServiceImpl implements IntweetService {

    @Autowired
    private IntweetDaoService intweetDaoService;

    @Autowired
    private ConversionService conversionService;

    @Override
    @HystrixCommand(fallbackMethod = "getLatestTweets_Fallback")
    public Tweets getLatestTweets(int offset, int limit) {
        Page<TweetsEntity> tweetsEntityList = intweetDaoService.findByCriteria(null, offset, limit);
        List<Tweet> tweetList = tweetsEntityList.getContent().stream()
                .map(TweetsEntityWrapper::new)
                .map(tweetsEntityWrapper -> conversionService.convert(tweetsEntityWrapper, Tweet.class))
                .collect(Collectors.toList());
        Tweets tweets = new Tweets();
        tweets.setTweets(tweetList);
        return tweets;
    }

    @Override
    public Tweets getEmployeeTweets(String employeeID, int offset, int limit) throws EmployeeNotFoundException {

        List<TweetsEntity> tweetsEntityList = intweetDaoService.findTweetsByEmployeeId(employeeID, offset, limit);
        if (tweetsEntityList.isEmpty()) {
            ErrorResponseUtil.throwEmployeeNotFoundException(employeeID);
        }
        List<Tweet> tweetList = tweetsEntityList.stream()
                .map(TweetsEntityWrapper::new)
                .map(tweetsEntityWrapper -> conversionService.convert(tweetsEntityWrapper, Tweet.class))
                .collect(Collectors.toList());

        Tweets tweets = new Tweets();
        tweets.setTweets(tweetList);
        return tweets;
    }

    @Override
    public Tweet postTweet(CreateTweetRequest createTweetRequest) {
        TweetsEntity tweetsEntity = conversionService.convert(createTweetRequest, TweetsEntity.class);
        TweetsEntityWrapper tweetsEntityWrapper = new TweetsEntityWrapper(tweetsEntity);
        intweetDaoService.saveTweet(tweetsEntity);
        return conversionService.convert(tweetsEntityWrapper, Tweet.class);
    }

    @Override
    public HttpStatus deleteTweet(int tweetID) {
        try {
            intweetDaoService.deleteTweet(tweetID);
            return HttpStatus.NO_CONTENT;
        } catch (EmptyResultDataAccessException ex) {
            return HttpStatus.NOT_FOUND;
        }
    }

    @Override
    public HttpStatus followEmployee(String employeeID, String followerID) {
        try {
            FollowersEntity followersEntity = new FollowersEntity();
            followersEntity.setEmployeeId(employeeID);
            followersEntity.setFollowerId(followerID);
            followersEntity.setCreatedDatetime(new Date());
            intweetDaoService.saveFollowersEntity(followersEntity);
            return HttpStatus.OK;
        } catch (DataIntegrityViolationException ex) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Override
    public HttpStatus unfollowEmployee(String employeeID, String followerID) {
        try {
            FollowersEntityPK followersEntityPK = new FollowersEntityPK();
            followersEntityPK.setEmployeeId(employeeID);
            followersEntityPK.setFollowerId(followerID);
            intweetDaoService.deleteFollower(followersEntityPK);
            return HttpStatus.OK;
        } catch (EmptyResultDataAccessException ex) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Override
    public List<Follower> getFollowers(String employeeID) throws EmployeeNotFoundException {
        EmployeesEntity employeesEntity = intweetDaoService.findEmployeeByEmployeeId(employeeID);
        if (ObjectUtils.isEmpty(employeesEntity)) {
            ErrorResponseUtil.throwEmployeeNotFoundException(employeeID);
        }
        List<FollowersEntity> followersEntityList = intweetDaoService.findAllByFollowerId(employeeID);
        List<Follower> followers = new ArrayList<>();
        for (FollowersEntity followersEntity : followersEntityList) {
            employeesEntity = intweetDaoService.findEmployeeByEmployeeId(followersEntity.getEmployeeId());
            Follower follower = new Follower();
            follower.setEmployeeId(employeesEntity.getEmployeeId());
            follower.setFirstName(employeesEntity.getFirstName());
            follower.setLastName(employeesEntity.getLastName());
            followers.add(follower);
        }
        return followers;
    }

    @Override
    public List<Follower> getFollowing(String employeeID) throws EmployeeNotFoundException {
        EmployeesEntity employeesEntity = intweetDaoService.findEmployeeByEmployeeId(employeeID);
        if (ObjectUtils.isEmpty(employeesEntity)) {
            ErrorResponseUtil.throwEmployeeNotFoundException(employeeID);
        }
        List<FollowersEntity> followingEntityList = intweetDaoService.findAllByFollowingId(employeeID);
        List<Follower> followingList = new ArrayList<>();
        for (FollowersEntity followingEntity : followingEntityList) {
            employeesEntity = intweetDaoService.findEmployeeByEmployeeId(followingEntity.getFollowerId());
            Follower following = new Follower();
            following.setEmployeeId(employeesEntity.getEmployeeId());
            following.setFirstName(employeesEntity.getFirstName());
            following.setLastName(employeesEntity.getLastName());
            followingList.add(following);
        }
        return followingList;
    }

    @SuppressWarnings("unused")
    private Tweets getLatestTweets_Fallback(int offset, int limit) {
        return new Tweets();
    }

}
