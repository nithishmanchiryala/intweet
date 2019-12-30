package com.intuit.intweet.resources;

import com.intuit.intweet.dao.entity.*;
import com.intuit.intweet.dao.repository.EmployeeRepository;
import com.intuit.intweet.dao.repository.FollowerRepository;
import com.intuit.intweet.dao.repository.TweetRepository;
import com.intuit.intweet.exceptions.EmployeeNotFoundException;
import com.intuit.intweet.models.request.CreateTweetRequest;
import com.intuit.intweet.models.response.Follower;
import com.intuit.intweet.models.response.Tweet;
import com.intuit.intweet.models.response.Tweets;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Matchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest
class IntweetResourceTest {

    @Autowired
    private IntweetResource intweetResource;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private TweetRepository tweetRepository;

    @MockBean
    private FollowerRepository followerRepository;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void getLatestTweetsTest_Feed() {
        TweetsEntity tweetsEntity = new TweetsEntity();
        tweetsEntity.setTweetId(1234);
        tweetsEntity.setTweet("Test tweet");
        tweetsEntity.setEmployeeId("485739857");
        tweetsEntity.setCreatedDatetime(new Date());
        tweetsEntity.setLastModifiedDatetime(new Date());
        final Page<TweetsEntity> page = new PageImpl(Arrays.asList(tweetsEntity));

        Mockito.when(tweetRepository.findAll(Matchers.anyObject(), (Pageable) Matchers.anyObject())).thenReturn(page);
        ResponseEntity<Tweets> tweetsResponseEntity = intweetResource.getLatestTweets(0, 5);
        Assert.assertEquals(tweetsResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getEmployeeTweetsTest() {
        List<TweetsEntity> tweetsEntityList = new ArrayList<>();
        TweetsEntity tweetsEntity = new TweetsEntity();
        tweetsEntity.setTweetId(1234);
        tweetsEntity.setTweet("Test tweet");
        tweetsEntity.setEmployeeId("485739857");
        tweetsEntity.setCreatedDatetime(new Date());
        tweetsEntity.setLastModifiedDatetime(new Date());
        tweetsEntityList.add(tweetsEntity);
        Mockito.when(tweetRepository.findByEmployeeId(anyString(), Matchers.anyObject())).thenReturn(tweetsEntityList);
        ResponseEntity<Tweets> tweetsResponseEntity = intweetResource.getMyTweets(tweetsEntity.getEmployeeId(), 0, 5);
        Assert.assertEquals(tweetsResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getTweetsTest() {
        EmployeesEntity employeesEntity = new EmployeesEntity();
        employeesEntity.setFirstName("Tony");
        employeesEntity.setLastName("Stark");
        employeesEntity.setEmployeeId("485739857");
        employeesEntity.setCreatedDatetime(new Date());
        employeesEntity.setLastModifiedDatetime(new Date());
        List<TweetsEntity> tweetsEntityList = new ArrayList<>();
        TweetsEntity tweetsEntity = new TweetsEntity();
        tweetsEntity.setTweetId(1234);
        tweetsEntity.setTweet("Test tweet");
        tweetsEntity.setEmployeeId("485739857");
        tweetsEntity.setCreatedDatetime(new Date());
        tweetsEntity.setLastModifiedDatetime(new Date());
        tweetsEntityList.add(tweetsEntity);

        List<FollowersEntity> followersEntityList = new ArrayList<>();
        FollowersEntity followersEntity = new FollowersEntity();
        followersEntity.setEmployeeId("24343");
        followersEntity.setFollowerId("65767");
        followersEntityList.add(followersEntity);
        Mockito.when(employeeRepository.findByEmployeeId(anyString())).thenReturn(employeesEntity);
        Mockito.when(tweetRepository.findByEmployeeId(anyString(), Matchers.anyObject())).thenReturn(tweetsEntityList);
        Mockito.when(followerRepository.findByEmployeeId(anyString())).thenReturn(followersEntityList);
        ResponseEntity<Tweets> tweetsResponseEntity = intweetResource.getTweets(tweetsEntity.getEmployeeId(), 0, 5);
        Assert.assertEquals(tweetsResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getEmployeeTweetsTest_Failure() {
        List<TweetsEntity> tweetsEntityList = new ArrayList<>();
        Mockito.when(tweetRepository.findByEmployeeId(anyString(), Matchers.anyObject())).thenReturn(tweetsEntityList);
        ResponseEntity<Tweets> tweetsResponseEntity = intweetResource.getMyTweets("38473894", 0, 5);
        Assert.assertEquals(tweetsResponseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void getEmployeeTweetsTest_Failure_isEmpty() throws Exception {
        exceptionRule.expect(EmployeeNotFoundException.class);
        ResponseEntity<Tweets> tweetsResponseEntity = intweetResource.getMyTweets("38473894", 0, 5);
    }

    @Test
    public void createTweetTest() {
        CreateTweetRequest createTweetRequest = new CreateTweetRequest();
        TweetsEntity tweetsEntity = new TweetsEntity();
        tweetsEntity.setTweetId(1234);
        tweetsEntity.setTweet("Test tweet");
        tweetsEntity.setEmployeeId("485739857");
        tweetsEntity.setCreatedDatetime(new Date());
        tweetsEntity.setLastModifiedDatetime(new Date());
        Mockito.when(tweetRepository.save(tweetsEntity)).thenReturn(tweetsEntity);
        ResponseEntity<Tweet> tweetsResponseEntity = intweetResource.createOrUpdateTweet(createTweetRequest, "1234");
        Assert.assertEquals(tweetsResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void deleteTweetTest() {
        Mockito.doNothing().when(tweetRepository).deleteById(isA(TweetsEntityPK.class));
        ResponseEntity responseEntity = intweetResource.deleteTweet("1234", 4567);
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void postTweet_Exception() {
        CreateTweetRequest createTweetRequest = new CreateTweetRequest();
        TweetsEntity tweetsEntity = new TweetsEntity();
        tweetsEntity.setTweetId(1234);
        tweetsEntity.setTweet("Test tweet");
        tweetsEntity.setEmployeeId("485739857");
        tweetsEntity.setCreatedDatetime(new Date());
        tweetsEntity.setLastModifiedDatetime(new Date());
        Mockito.doThrow(DataIntegrityViolationException.class).when(tweetRepository).save(isA(TweetsEntity.class));
        ResponseEntity responseEntity = intweetResource.createOrUpdateTweet(createTweetRequest, "34343");
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void postTweetIdNull_Exception() {
        CreateTweetRequest createTweetRequest = new CreateTweetRequest();
        TweetsEntity tweetsEntity = new TweetsEntity();
        tweetsEntity.setTweet("Test tweet");
        tweetsEntity.setEmployeeId("485739857");
        tweetsEntity.setCreatedDatetime(new Date());
        tweetsEntity.setLastModifiedDatetime(new Date());
        Mockito.doThrow(DataIntegrityViolationException.class).when(tweetRepository).save(isA(TweetsEntity.class));
        ResponseEntity responseEntity = intweetResource.createOrUpdateTweet(createTweetRequest, "");
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }


    @Test
    public void deleteTweetTest_fail() {
        Mockito.doThrow(EmptyResultDataAccessException.class).when(tweetRepository).deleteById(isA(TweetsEntityPK.class));
        ResponseEntity responseEntity = intweetResource.deleteTweet("32873", 1234);
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void followEmployeeTest() {
        FollowersEntity followersEntity = new FollowersEntity();
        Mockito.when(followerRepository.save(Matchers.anyObject())).thenReturn(followersEntity);
        ResponseEntity responseEntity = intweetResource.followEmployee("1234", "1234");
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void unfollowEmployeeTest() {
        Mockito.doNothing().when(followerRepository).deleteById(isA(FollowersEntityPK.class));
        ResponseEntity responseEntity = intweetResource.unfollowEmployee("1234", "1234");
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void followEmployeeTest_Exception() {
        Mockito.when(followerRepository.save(Matchers.anyObject())).thenThrow(DataIntegrityViolationException.class);
        ResponseEntity responseEntity = intweetResource.followEmployee("1234", "1234");
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void unfollowEmployeeTest_Exception() {
        Mockito.doThrow(EmptyResultDataAccessException.class).when(followerRepository).deleteById(isA(FollowersEntityPK.class));
        ResponseEntity responseEntity = intweetResource.unfollowEmployee("1234", "1234");
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getFollowersTest() {
        FollowersEntity followersEntity = new FollowersEntity();
        followersEntity.setFollowerId("1234");
        followersEntity.setEmployeeId("2345");
        TweetsEntity tweetsEntity = new TweetsEntity();
        CreateTweetRequest createTweetRequest = new CreateTweetRequest();
        createTweetRequest.setEmployeeId("1234");
        createTweetRequest.setTweet("helloworld");
        EmployeesEntity employeesEntity = new EmployeesEntity();
        employeesEntity.setEmployeeId("1234");
        employeesEntity.setFollowersByEmployeeId(Arrays.asList(followersEntity));
        employeesEntity.setTweetsByEmployeeId(Arrays.asList(tweetsEntity));
        Mockito.when(followerRepository.findByFollowerId(anyString())).thenReturn(Arrays.asList(followersEntity));
        Mockito.when(employeeRepository.findByEmployeeId(anyString())).thenReturn(employeesEntity);
        ResponseEntity responseEntity = intweetResource.getFollowers("1234");
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(responseEntity.getBody());
    }

    @Test
    public void getFollowingTest() {
        FollowersEntity followersEntity = new FollowersEntity();
        followersEntity.setFollowerId("1234");
        followersEntity.setEmployeeId("2345");
        TweetsEntity tweetsEntity = new TweetsEntity();
        CreateTweetRequest createTweetRequest = new CreateTweetRequest();
        createTweetRequest.setEmployeeId("1234");
        createTweetRequest.setTweet("helloworld");
        EmployeesEntity employeesEntity = new EmployeesEntity();
        employeesEntity.setEmployeeId("1234");
        employeesEntity.setFollowersByEmployeeId(Arrays.asList(followersEntity));
        employeesEntity.setTweetsByEmployeeId(Arrays.asList(tweetsEntity));
        Mockito.when(followerRepository.findByEmployeeId(anyString())).thenReturn(Arrays.asList(followersEntity));
        Mockito.when(employeeRepository.findByEmployeeId(anyString())).thenReturn(employeesEntity);
        ResponseEntity responseEntity = intweetResource.getFollowing("1234");
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(responseEntity.getBody());
    }

    @Test
    public void getFollowersTest_Exception() {
        exceptionRule.expect(EmployeeNotFoundException.class);
        intweetResource.getFollowers("1234");
    }

    @Test
    public void getFollowingTest_Exception() {
        exceptionRule.expect(EmployeeNotFoundException.class);
        intweetResource.getFollowing("1234");
    }

    @Test
    public void getTweetsTest_Exception() {
        exceptionRule.expect(EmployeeNotFoundException.class);
        ResponseEntity<Tweets> responseEntity = intweetResource.getTweets("1234", 0, 5);
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void entityTest() {
        EmployeesEntity employeesEntity = new EmployeesEntity();
        employeesEntity.setTweetsByEmployeeId(Arrays.asList(new TweetsEntity()));
        employeesEntity.setEmployeeId("1234");
        employeesEntity.setFollowersByEmployeeId(Arrays.asList(new FollowersEntity()));
        employeesEntity.setFirstName("Tony");
        employeesEntity.setLastName("Stark");
        employeesEntity.setCreatedDatetime(new Date());
        employeesEntity.setLastModifiedDatetime(new Date());
        employeesEntity.setUidpk(1234);
        employeesEntity.getEmployeeId();
        employeesEntity.getFirstName();
        employeesEntity.getLastName();
        employeesEntity.getCreatedDatetime();
        employeesEntity.getFollowersByEmployeeId();
        employeesEntity.getTweetsByEmployeeId();

        FollowersEntityPK followersEntityPK = new FollowersEntityPK();
        followersEntityPK.setEmployeeId("357438924");
        followersEntityPK.setFollowerId("564546565");
        followersEntityPK.getEmployeeId();
        followersEntityPK.hashCode();
        followersEntityPK.equals(followersEntityPK);
        followersEntityPK.equals(null);
        followersEntityPK.equals(new ArrayList());
        FollowersEntityPK followersEntityPK1 = new FollowersEntityPK();
        followersEntityPK1.setEmployeeId("357438924");
        followersEntityPK1.setFollowerId("564546565");
        followersEntityPK.equals(followersEntityPK1);
        followersEntityPK1.setEmployeeId("3574d38924");
        followersEntityPK1.setFollowerId("564546565");
        followersEntityPK.equals(followersEntityPK1);
        followersEntityPK1.setEmployeeId("357438924");
        followersEntityPK1.setFollowerId("564546c565");
        followersEntityPK.equals(followersEntityPK1);
        followersEntityPK.getFollowerId();
        TweetsEntity tweetsEntity = new TweetsEntity();
        Date date = new Date();
        tweetsEntity.setTweetId(1234);
        tweetsEntity.setTweet("Test tweet");
        tweetsEntity.setEmployeeId("485739857");
        tweetsEntity.setCreatedDatetime(date);
        tweetsEntity.setLastModifiedDatetime(date);
        tweetsEntity.getCreatedDatetime();
        tweetsEntity.getLastModifiedDatetime();
        tweetsEntity.hashCode();
        tweetsEntity.equals(null);
        tweetsEntity.equals(new ArrayList());
        tweetsEntity.equals(tweetsEntity);
        TweetsEntity tweetsEntity1 = new TweetsEntity();
        tweetsEntity1.setTweetId(1234);
        tweetsEntity1.setTweet("Test tweet");
        tweetsEntity1.setEmployeeId("485739857");
        tweetsEntity1.setCreatedDatetime(date);
        tweetsEntity1.setLastModifiedDatetime(date);
        tweetsEntity.equals(tweetsEntity1);
        tweetsEntity1.setTweetId(12354);
        tweetsEntity1.setTweet("Test tweet");
        tweetsEntity1.setEmployeeId("485739857");
        tweetsEntity1.setCreatedDatetime(date);
        tweetsEntity1.setLastModifiedDatetime(date);
        tweetsEntity.equals(tweetsEntity1);
        tweetsEntity1.setTweetId(1234);
        tweetsEntity1.setTweet("Test dtweet");
        tweetsEntity1.setEmployeeId("485739857");
        tweetsEntity1.setCreatedDatetime(date);
        tweetsEntity1.setLastModifiedDatetime(date);
        tweetsEntity.equals(tweetsEntity1);
        tweetsEntity1.setTweetId(1234);
        tweetsEntity1.setTweet("Test tweet");
        tweetsEntity1.setEmployeeId("4857d39857");
        tweetsEntity1.setCreatedDatetime(date);
        tweetsEntity1.setLastModifiedDatetime(date);
        tweetsEntity.equals(tweetsEntity1);
        tweetsEntity1.setTweetId(1234);
        tweetsEntity1.setTweet("Test tweet");
        tweetsEntity1.setEmployeeId("485739857");
        tweetsEntity1.setCreatedDatetime(new Date());
        tweetsEntity1.setLastModifiedDatetime(date);
        tweetsEntity.equals(tweetsEntity1);
        tweetsEntity1.setTweetId(1234);
        tweetsEntity1.setTweet("Test tweet");
        tweetsEntity1.setEmployeeId("485739857");
        tweetsEntity1.setCreatedDatetime(date);
        tweetsEntity1.setLastModifiedDatetime(new Date());
        tweetsEntity.equals(tweetsEntity1);
    }

    @Test
    public void modelClassTest() {
        Follower follower = new Follower();
        follower.getEmployeeId();
        follower.getFirstName();
        follower.getLastName();

        Tweet tweet = new Tweet();
        tweet.getTweetId();
        tweet.getTweet();

        Tweets tweets = new Tweets();
        tweets.getTweets();
    }

}