package com.intuit.intweet.services.impl;

import com.intuit.intweet.dao.entity.*;
import com.intuit.intweet.dao.service.IntweetDaoService;
import com.intuit.intweet.dto.TweetsEntityWrapper;
import com.intuit.intweet.exceptions.CustomException;
import com.intuit.intweet.exceptions.ExceptionThrower;
import com.intuit.intweet.models.request.CreateTweetRequest;
import com.intuit.intweet.models.response.Follower;
import com.intuit.intweet.models.response.Followers;
import com.intuit.intweet.models.response.Tweet;
import com.intuit.intweet.models.response.Tweets;
import com.intuit.intweet.services.IntweetService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IntweetServiceImpl implements IntweetService {

    private static final Logger logger = LoggerFactory.getLogger(IntweetServiceImpl.class);

    @Autowired
    private IntweetDaoService intweetDaoService;

    @Autowired
    private ConversionService conversionService;

    @Override
    @HystrixCommand(fallbackMethod = "getTweets_Fallback")
    public Tweets getLatestTweets(int offset, int limit) {
        Page<TweetsEntity> tweetsEntityList = intweetDaoService.findByCriteria(null, offset, limit);
        return convertTweets(tweetsEntityList.getContent());
    }

    @Override
    @HystrixCommand(fallbackMethod = "getTweets_Fallback")
    public Tweets getTweets(String employeeID, int offset, int limit) throws CustomException {
        getEmployee(employeeID);

        List<FollowersEntity> followingEntityList = intweetDaoService.findAllByFollowingId(employeeID);
        List<String> followingList = new ArrayList<>();
        followingEntityList.forEach(e -> followingList.add(e.getFollowerId()));

        List<TweetsEntity> tweetsEntityList = intweetDaoService.findTweetsByEmployeeIdIn(followingList, offset, limit);
        return convertTweets(tweetsEntityList);
    }

    @Override
    @HystrixCommand(fallbackMethod = "getTweets_Fallback")
    public Tweets getEmployeeTweets(String employeeID, int offset, int limit) throws CustomException {

        List<TweetsEntity> tweetsEntityList = intweetDaoService.findTweetsByEmployeeId(employeeID, offset, limit);
        if (tweetsEntityList.isEmpty()) {
            throwEmployeeNotFoundException();
        }
        return convertTweets(tweetsEntityList);
    }

    @Override
    public void postTweet(CreateTweetRequest createTweetRequest, String tweetID) throws CustomException {
        try {
            TweetsEntity tweetsEntity = new TweetsEntity();
            tweetsEntity.setEmployeeId(createTweetRequest.getEmployeeId());
            tweetsEntity.setTweet(createTweetRequest.getTweet());
            if (StringUtils.isNotBlank(tweetID)) {
                tweetsEntity.setTweetId(Integer.parseInt(tweetID));
            }
            intweetDaoService.saveTweet(tweetsEntity);
        } catch (DataIntegrityViolationException ex) {
            throwEmployeeNotFoundException();
        }
    }

    @Override
    public void deleteTweet(String employeeID, int tweetID) throws CustomException {
        try {
            TweetsEntityPK tweetsEntityPK = new TweetsEntityPK();
            tweetsEntityPK.setEmployeeId(employeeID);
            tweetsEntityPK.setTweetId(tweetID);
            intweetDaoService.deleteTweet(tweetsEntityPK);
        } catch (EmptyResultDataAccessException ex) {
            ExceptionThrower exceptionThrower = new ExceptionThrower();
            exceptionThrower.throwTweetNotFoundException();
        }
    }

    @Override
    public void followEmployee(String employeeID, String followerID) throws CustomException {
        if (employeeID.equalsIgnoreCase(followerID)) {
            ExceptionThrower exceptionThrower = new ExceptionThrower();
            exceptionThrower.throwBadRequestException();
        }
        try {
            FollowersEntity followersEntity = new FollowersEntity();
            followersEntity.setEmployeeId(employeeID);
            followersEntity.setFollowerId(followerID);
            followersEntity.setCreatedDatetime(new Date());
            intweetDaoService.saveFollowersEntity(followersEntity);
        } catch (DataIntegrityViolationException ex) {
            throwEmployeeNotFoundException();
        }
    }

    @Override
    public void unfollowEmployee(String employeeID, String followerID) throws CustomException {
        try {
            FollowersEntityPK followersEntityPK = new FollowersEntityPK();
            followersEntityPK.setEmployeeId(employeeID);
            followersEntityPK.setFollowerId(followerID);
            intweetDaoService.deleteFollower(followersEntityPK);
        } catch (EmptyResultDataAccessException ex) {
            ExceptionThrower exceptionThrower = new ExceptionThrower();
            exceptionThrower.throwNotFollowingEmployeeException();
        }
    }

    @Override
    public Followers getFollowers(String employeeID) throws CustomException {
        getEmployee(employeeID);
        EmployeesEntity employeesEntity;
        List<FollowersEntity> entityList = intweetDaoService.findAllByFollowerId(employeeID);
        Followers followers = new Followers();
        List<Follower> resultList = new ArrayList<>();
        for (FollowersEntity followersEntity : entityList) {
            employeesEntity = intweetDaoService.findEmployeeByEmployeeId(followersEntity.getEmployeeId());
            Follower follower = setFollower(employeesEntity);
            resultList.add(follower);
        }
        followers.setFollowers(resultList);
        return followers;
    }

    @Override
    public Followers getFollowing(String employeeID) throws CustomException {
        getEmployee(employeeID);
        EmployeesEntity employeesEntity;
        List<FollowersEntity> entityList = intweetDaoService.findAllByFollowingId(employeeID);
        List<Follower> resultList = new ArrayList<>();
        Followers following = new Followers();
        for (FollowersEntity followingEntity : entityList) {
            employeesEntity = intweetDaoService.findEmployeeByEmployeeId(followingEntity.getFollowerId());
            Follower follower = setFollower(employeesEntity);
            resultList.add(follower);
        }
        following.setFollowers(resultList);
        return following;
    }


    private void getEmployee(String employeeID) throws CustomException {
        EmployeesEntity employeesEntity = intweetDaoService.findEmployeeByEmployeeId(employeeID);
        if (ObjectUtils.isEmpty(employeesEntity)) {
            throwEmployeeNotFoundException();
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

    private void throwEmployeeNotFoundException() throws CustomException {
        logger.error("Employee not found");
        ExceptionThrower exceptionThrower = new ExceptionThrower();
        exceptionThrower.throwEmployeeNotFoundException();
    }

    @SuppressWarnings("unused")
    public Tweets getTweets_Fallback(int offset, int limit) {
        logger.error("Get tweets api is down, circuit opened");
        return new Tweets();
    }

    @SuppressWarnings("unused")
    public Tweets getTweets_Fallback(String employeeID, int offset, int limit) {
        logger.error("Get tweets api is down, circuit opened");
        return new Tweets();
    }
}
