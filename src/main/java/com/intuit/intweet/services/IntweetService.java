package com.intuit.intweet.services;

import com.intuit.intweet.exceptions.EmployeeNotFoundException;
import com.intuit.intweet.models.request.CreateTweetRequest;
import com.intuit.intweet.models.response.Follower;
import com.intuit.intweet.models.response.Tweet;
import com.intuit.intweet.models.response.Tweets;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface IntweetService {

    Tweets getLatestTweets(int offset, int limit);

    Tweets getTweets(String employeeID, int offset, int limit) throws EmployeeNotFoundException;

    Tweets getEmployeeTweets(String employeeID, int offset, int limit) throws EmployeeNotFoundException;

    Tweet postTweet(CreateTweetRequest createTweetRequest);

    HttpStatus deleteTweet(String employeeID, int tweetID);

    HttpStatus followEmployee(String employeeID, String followerID);

    HttpStatus unfollowEmployee(String employeeID, String followerID);

    List<Follower> getFollowers(String employeeID) throws EmployeeNotFoundException;

    List<Follower> getFollowing(String employeeID) throws EmployeeNotFoundException;
}
