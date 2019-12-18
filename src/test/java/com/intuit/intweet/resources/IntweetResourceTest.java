package com.intuit.intweet.resources;

import com.intuit.intweet.dao.entity.*;
import com.intuit.intweet.dao.repository.EmployeeRepository;
import com.intuit.intweet.dao.repository.FollowerRepository;
import com.intuit.intweet.dao.repository.TweetRepository;
import com.intuit.intweet.exceptions.EmployeeNotFoundException;
import com.intuit.intweet.exceptions.ErrorResponseUtil;
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

import java.util.*;

import static org.mockito.ArgumentMatchers.isA;

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
        tweetsEntity.setTweetResponsesByTweetId(Arrays.asList(new TweetResponsesEntity()));
        final Page<TweetsEntity> page = new PageImpl(Arrays.asList(tweetsEntity));

        Mockito.when(tweetRepository.findAll(Matchers.anyObject(), (Pageable) Matchers.anyObject())).thenReturn(page);
        ResponseEntity<Tweets> tweetsResponseEntity = intweetResource.getLatestTweets(0, 5);
        Assert.assertEquals(tweetsResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getEmployeeTweetsTest() throws EmployeeNotFoundException {
        List<TweetsEntity> tweetsEntityList = new ArrayList<>();
        TweetsEntity tweetsEntity = new TweetsEntity();
        tweetsEntity.setTweetId(1234);
        tweetsEntity.setTweet("Test tweet");
        tweetsEntity.setEmployeeId("485739857");
        tweetsEntity.setCreatedDatetime(new Date());
        tweetsEntity.setLastModifiedDatetime(new Date());
        tweetsEntityList.add(tweetsEntity);
        Mockito.when(tweetRepository.findByEmployeeId(Matchers.anyString(), Matchers.anyObject())).thenReturn(tweetsEntityList);
        ResponseEntity<Tweets> tweetsResponseEntity = intweetResource.getEmployeeTweets(tweetsEntity.getEmployeeId(), 0, 5);
        Assert.assertEquals(tweetsResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getEmployeeTweetsTest_Failure() {
        List<TweetsEntity> tweetsEntityList = new ArrayList<>();
        Mockito.when(tweetRepository.findByEmployeeId(Matchers.anyString(), Matchers.anyObject())).thenReturn(tweetsEntityList);
        ResponseEntity<Tweets> tweetsResponseEntity = intweetResource.getEmployeeTweets("38473894", 0, 5);
        Assert.assertEquals(tweetsResponseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    /*@Test
    public void getEmployeeTweetsTest_Exception() throws EmployeeNotFoundException {
        Mockito.when()
        ResponseEntity<Tweets> tweetsResponseEntity = intweetResource.getEmployeeTweets("38473894", 0, 5);
        exceptionRule.expect(EmployeeNotFoundException.class);
    }*/

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
        ResponseEntity<Tweet> tweetsResponseEntity = intweetResource.createTweet(createTweetRequest);
        Assert.assertEquals(tweetsResponseEntity.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(Objects.requireNonNull(tweetsResponseEntity.getBody()).getEmployeeId(), createTweetRequest.getEmployeeId());
    }

    @Test
    public void deleteTweetTest() {
        Mockito.doNothing().when(tweetRepository).deleteById(isA(Integer.class));
        ResponseEntity responseEntity = intweetResource.deleteTweet(1234);
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    public void deleteTweetTest_fail() {
        Mockito.doThrow(EmptyResultDataAccessException.class).when(tweetRepository).deleteById(isA(Integer.class));
        ResponseEntity responseEntity = intweetResource.deleteTweet(1234);
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
    public void followEmployeeTest_Exception() {
        Mockito.when(followerRepository.save(Matchers.anyObject())).thenThrow(DataIntegrityViolationException.class);
        ResponseEntity responseEntity = intweetResource.followEmployee("1234", "1234");
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getFollowersTest() {
        FollowersEntity followersEntity = new FollowersEntity();
        followersEntity.setUidpk(1);
        followersEntity.setFollowerId("1234");
        followersEntity.setEmployeeId("2345");
        followersEntity.setCreatedDatetime(new Date());
        TweetsEntity tweetsEntity = new TweetsEntity();
        CreateTweetRequest createTweetRequest = new CreateTweetRequest();
        createTweetRequest.setEmployeeId("1234");
        createTweetRequest.setTweet("helloworld");
        EmployeesEntity employeesEntity = new EmployeesEntity();
        employeesEntity.setEmployeeId("1234");
        employeesEntity.setFollowersByEmployeeId(Arrays.asList(followersEntity));
        employeesEntity.setTweetsByEmployeeId(Arrays.asList(tweetsEntity));
        Mockito.when(followerRepository.findByFollowerId(Matchers.anyString())).thenReturn(Arrays.asList(followersEntity));
        Mockito.when(employeeRepository.findByEmployeeId(Matchers.anyString())).thenReturn(employeesEntity);
        ResponseEntity responseEntity = intweetResource.getFollowers("1234");
        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(responseEntity.getBody());
    }

    @Test
    public void getFollowersTest_Exception() {
        exceptionRule.expect(EmployeeNotFoundException.class);
        ResponseEntity responseEntity = intweetResource.getFollowers("1234");

    }
    @Test
    public void entityTest() {
        EmployeesEntity employeesEntity = new EmployeesEntity();
        employeesEntity.setTweetsByEmployeeId(Arrays.asList(new TweetsEntity()));
        employeesEntity.setEmployeeId("1234");
        employeesEntity.setFollowersByEmployeeId(Arrays.asList(new FollowersEntity()));
        employeesEntity.setFirstName("abhi");
        employeesEntity.setLastName("nav");
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
        followersEntityPK.getFollowerId();
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