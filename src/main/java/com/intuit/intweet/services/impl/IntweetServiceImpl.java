package com.intuit.intweet.services.impl;

import com.intuit.intweet.dao.entity.*;
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
import org.apache.commons.lang.StringUtils;
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
        return convertTweets(tweetsEntityList.getContent());
    }

    @Override
    public Tweets getTweets(String employeeID, int offset, int limit) throws EmployeeNotFoundException {
        getEmployee(employeeID);

        List<FollowersEntity> followingEntityList = intweetDaoService.findAllByFollowingId(employeeID);
        List<String> followingList = new ArrayList<>();
        followingEntityList.forEach(e -> followingList.add(e.getFollowerId()));

        List<TweetsEntity> tweetsEntityList = intweetDaoService.findTweetsByEmployeeIdIn(followingList, offset, limit);
        return convertTweets(tweetsEntityList);
    }

    @Override
    public Tweets getEmployeeTweets(String employeeID, int offset, int limit) throws EmployeeNotFoundException {

        List<TweetsEntity> tweetsEntityList = intweetDaoService.findTweetsByEmployeeId(employeeID, offset, limit);
        if (tweetsEntityList.isEmpty()) {
            ErrorResponseUtil.throwEmployeeNotFoundException(employeeID);
        }
        return convertTweets(tweetsEntityList);
    }

    @Override
    public HttpStatus postTweet(CreateTweetRequest createTweetRequest, String tweetID) {
        try {
            TweetsEntity tweetsEntity = new TweetsEntity();
            tweetsEntity.setEmployeeId(createTweetRequest.getEmployeeId());
            tweetsEntity.setTweet(createTweetRequest.getTweet());
            if (StringUtils.isNotBlank(tweetID)) {
                tweetsEntity.setTweetId(Integer.parseInt(tweetID));
            }
            intweetDaoService.saveTweet(tweetsEntity);
            return HttpStatus.OK;
        } catch (DataIntegrityViolationException ex) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Override
    public HttpStatus deleteTweet(String employeeID, int tweetID) {
        try {
            TweetsEntityPK tweetsEntityPK = new TweetsEntityPK();
            tweetsEntityPK.setEmployeeId(employeeID);
            tweetsEntityPK.setTweetId(tweetID);
            intweetDaoService.deleteTweet(tweetsEntityPK);
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
        getEmployee(employeeID);
        EmployeesEntity employeesEntity;
        List<FollowersEntity> entityList = intweetDaoService.findAllByFollowerId(employeeID);
        List<Follower> resultList = new ArrayList<>();
        for (FollowersEntity followersEntity : entityList) {
            employeesEntity = intweetDaoService.findEmployeeByEmployeeId(followersEntity.getEmployeeId());
            Follower follower = setFollower(employeesEntity);
            resultList.add(follower);
        }
        return resultList;
    }

    @Override
    public List<Follower> getFollowing(String employeeID) throws EmployeeNotFoundException {
        getEmployee(employeeID);
        EmployeesEntity employeesEntity;
        List<FollowersEntity> entityList = intweetDaoService.findAllByFollowingId(employeeID);
        List<Follower> resultList = new ArrayList<>();
        for (FollowersEntity followingEntity : entityList) {
            employeesEntity = intweetDaoService.findEmployeeByEmployeeId(followingEntity.getFollowerId());
            Follower follower = setFollower(employeesEntity);
            resultList.add(follower);
        }
        return resultList;
    }


    private void getEmployee(String employeeID) throws EmployeeNotFoundException {
        EmployeesEntity employeesEntity = intweetDaoService.findEmployeeByEmployeeId(employeeID);
        if (ObjectUtils.isEmpty(employeesEntity)) {
            ErrorResponseUtil.throwEmployeeNotFoundException(employeeID);
        }
    }

    private Tweets convertTweets(List<TweetsEntity> tweetsEntityList) {
        List<Tweet> tweetList = tweetsEntityList.stream()
                .map(TweetsEntityWrapper::new)
                .map(tweetsEntityWrapper -> conversionService.convert(tweetsEntityWrapper, Tweet.class))
                .collect(Collectors.toList());

        Tweets tweets = new Tweets();
        tweets.setTweets(tweetList);
        return tweets;
    }

    private Follower setFollower(EmployeesEntity employeesEntity) {
        Follower follower = new Follower();
        follower.setEmployeeId(employeesEntity.getEmployeeId());
        follower.setFirstName(employeesEntity.getFirstName());
        follower.setLastName(employeesEntity.getLastName());
        return follower;
    }

    @SuppressWarnings("unused")
    private Tweets getLatestTweets_Fallback(int offset, int limit) {
        return new Tweets();
    }

}
