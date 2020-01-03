package com.intuit.intweet.services;

import com.intuit.intweet.exceptions.CustomException;
import com.intuit.intweet.models.request.CreateTweetRequest;
import com.intuit.intweet.models.response.Followers;
import com.intuit.intweet.models.response.Tweets;

public interface IntweetService {

    Tweets getLatestTweets(int offset, int limit);

    Tweets getTweets(String employeeID, int offset, int limit) throws CustomException;

    Tweets getEmployeeTweets(String employeeID, int offset, int limit) throws CustomException;

    void postTweet(CreateTweetRequest createTweetRequest, String tweetID) throws CustomException;

    void deleteTweet(String employeeID, int tweetID) throws CustomException;

    void followEmployee(String employeeID, String followerID) throws CustomException;

    void unfollowEmployee(String employeeID, String followerID) throws CustomException;

    Followers getFollowers(String employeeID) throws CustomException;

    Followers getFollowing(String employeeID) throws CustomException;
}
