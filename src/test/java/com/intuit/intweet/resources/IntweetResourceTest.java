package com.intuit.intweet.resources;

import com.intuit.intweet.dao.entity.*;
import com.intuit.intweet.dao.repository.EmployeeRepository;
import com.intuit.intweet.dao.repository.FollowerRepository;
import com.intuit.intweet.dao.repository.TweetRepository;
import com.intuit.intweet.dto.TweetsEntityWrapper;
import com.intuit.intweet.exceptions.CustomException;
import com.intuit.intweet.models.request.CreateTweetRequest;
import com.intuit.intweet.models.response.Follower;
import com.intuit.intweet.models.response.Followers;
import com.intuit.intweet.models.response.Tweet;
import com.intuit.intweet.models.response.Tweets;
import com.intuit.intweet.services.impl.IntweetServiceImpl;
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
        Tweets tweetsResponseEntity = intweetResource.getAllTweets(0, 5);
        Assert.assertNotNull(tweetsResponseEntity);
    }

    @Test
    public void getEmployeeTweetsTest() throws CustomException {
        List<TweetsEntity> tweetsEntityList = new ArrayList<>();
        TweetsEntity tweetsEntity = new TweetsEntity();
        tweetsEntity.setTweetId(1234);
        tweetsEntity.setTweet("Test tweet");
        tweetsEntity.setEmployeeId("485739857");
        tweetsEntity.setCreatedDatetime(new Date());
        tweetsEntity.setLastModifiedDatetime(new Date());
        tweetsEntityList.add(tweetsEntity);
        Mockito.when(tweetRepository.findByEmployeeId(anyString(), Matchers.anyObject())).thenReturn(tweetsEntityList);
        Tweets tweetsResponseEntity = intweetResource.getMyTweets(tweetsEntity.getEmployeeId(), 0, 5);
        Assert.assertNotNull(tweetsResponseEntity);
    }

    @Test
    public void getTweetsTest() throws CustomException {
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
        Tweets tweetsResponseEntity = intweetResource.getTweets(tweetsEntity.getEmployeeId(), 0, 5);
        Assert.assertNotNull(tweetsResponseEntity);
    }

    @Test
    public void getEmployeeTweetsTest_Failure() {
        List<TweetsEntity> tweetsEntityList = new ArrayList<>();
        getTweets_Fallback();
        Mockito.when(tweetRepository.findByEmployeeId(anyString(), Matchers.anyObject())).thenReturn(tweetsEntityList);
        try {
            Tweets tweetsResponseEntity = intweetResource.getMyTweets("38473894", 0, 5);
        } catch (CustomException e) {
            Assert.assertEquals(404, e.getCode());
            Assert.assertEquals("Employee not found", e.getMessage());
        }
    }

    @Test
    public void getEmployeeTweetsTest_Failure_isEmpty() {
        try {
            Tweets tweetsResponseEntity = intweetResource.getMyTweets("38473894", 0, 5);
        } catch (CustomException e) {
            Assert.assertEquals(404, e.getCode());
        }
    }

    @Test
    public void createTweetTest() throws CustomException {
        CreateTweetRequest createTweetRequest = new CreateTweetRequest();
        TweetsEntity tweetsEntity = new TweetsEntity();
        tweetsEntity.setTweetId(1234);
        tweetsEntity.setTweet("Test tweet");
        tweetsEntity.setEmployeeId("485739857");
        tweetsEntity.setCreatedDatetime(new Date());
        tweetsEntity.setLastModifiedDatetime(new Date());
        Mockito.when(tweetRepository.save(tweetsEntity)).thenReturn(tweetsEntity);
        intweetResource.createOrUpdateTweet(createTweetRequest, "1234");
    }

    @Test
    public void deleteTweetTest() throws CustomException {
        Mockito.doNothing().when(tweetRepository).deleteById(isA(TweetsEntityPK.class));
        intweetResource.deleteTweet("1234", 4567);
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
        try {
            intweetResource.createOrUpdateTweet(createTweetRequest, "34343");
        } catch (CustomException e) {
            Assert.assertEquals(404, e.getCode());
        }
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
        try {
            intweetResource.createOrUpdateTweet(createTweetRequest, "");
        } catch (CustomException e) {
            Assert.assertEquals(404, e.getCode());
        }
    }


    @Test
    public void deleteTweetTest_fail() {
        Mockito.doThrow(EmptyResultDataAccessException.class).when(tweetRepository).deleteById(isA(TweetsEntityPK.class));
        try {
            intweetResource.deleteTweet("32873", 1234);
        } catch (CustomException e) {
            Assert.assertEquals(404, e.getCode());
        }
    }

    @Test
    public void followEmployeeTest() throws CustomException {
        FollowersEntity followersEntity = new FollowersEntity();
        Mockito.when(followerRepository.save(Matchers.anyObject())).thenReturn(followersEntity);
        intweetResource.followEmployee("1234", "2345");
    }

    @Test
    public void followEmployeeTest_Failure() throws CustomException {
        FollowersEntity followersEntity = new FollowersEntity();
        Mockito.when(followerRepository.save(Matchers.anyObject())).thenReturn(followersEntity);
        try {
            intweetResource.followEmployee("1234", "1234");
        } catch (CustomException e) {
            Assert.assertEquals(400, e.getCode());
        }
    }

    @Test
    public void unfollowEmployeeTest() throws CustomException {
        Mockito.doNothing().when(followerRepository).deleteById(isA(FollowersEntityPK.class));
        intweetResource.unfollowEmployee("1234", "1234");
    }

    @Test
    public void followEmployeeTest_Exception() {
        Mockito.when(followerRepository.save(Matchers.anyObject())).thenThrow(DataIntegrityViolationException.class);
        try {
            intweetResource.followEmployee("1234", "3456");
        } catch (CustomException e) {
            Assert.assertEquals(404, e.getCode());
        }
    }

    @Test
    public void unfollowEmployeeTest_Exception() {
        Mockito.doThrow(EmptyResultDataAccessException.class).when(followerRepository).deleteById(isA(FollowersEntityPK.class));
        try {
            intweetResource.unfollowEmployee("1234", "1234");
        } catch (CustomException e) {
            Assert.assertEquals(400, e.getCode());
        }
    }

    @Test
    public void getFollowersTest() throws CustomException {
        FollowersEntity followersEntity = new FollowersEntity();
        followersEntity.setFollowerId("1234");
        followersEntity.setEmployeeId("2345");
        followersEntity.getCreatedDatetime();
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
        Followers followers = intweetResource.getFollowers("1234");
        Assert.assertNotNull(followers);
    }

    @Test
    public void getFollowingTest() throws CustomException {
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
        Followers followers = intweetResource.getFollowing("1234");
        Assert.assertNotNull(followers);
        Assert.assertEquals(followers.getFollowers().get(0).getEmployeeId(), employeesEntity.getEmployeeId());
    }

    @Test
    public void getFollowersTest_Exception() {
        try {
            intweetResource.getFollowers("1234");
        } catch (CustomException e) {
            Assert.assertEquals(404, e.getCode());
            Assert.assertEquals("Employee not found", e.getMessage());
        }

    }

    @Test
    public void getFollowingTest_Exception() {
        try {
            intweetResource.getFollowing("1234");
        } catch (CustomException e) {
            Assert.assertEquals(404, e.getCode());
        }
    }

    @Test
    public void getTweetsTest_Exception() {
        exceptionRule.expect(CustomException.class);
        try {
            Tweets tweets = intweetResource.getTweets("1234", 0, 5);
        } catch (CustomException e) {
            Assert.assertEquals(404, e.getCode());
        }
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
        employeesEntity.getEmployeeId();
        employeesEntity.getFirstName();
        employeesEntity.getLastName();
        employeesEntity.getCreatedDatetime();
        employeesEntity.getFollowersByEmployeeId();
        employeesEntity.getTweetsByEmployeeId();
        employeesEntity.getLastModifiedDatetime();

        TweetsEntityPK tweetsEntityPK = new TweetsEntityPK();
        tweetsEntityPK.setEmployeeId("394738");
        tweetsEntityPK.setTweetId(123);
        tweetsEntityPK.getEmployeeId();
        tweetsEntityPK.getTweetId();
        tweetsEntityPK.hashCode();
        tweetsEntityPK.equals(tweetsEntityPK);
        tweetsEntityPK.equals(null);
        tweetsEntityPK.equals(new ArrayList());
        TweetsEntityPK tweetsEntityPK1 = new TweetsEntityPK();
        tweetsEntityPK1.setEmployeeId("394738");
        tweetsEntityPK1.setTweetId(123);
        tweetsEntityPK.equals(tweetsEntityPK1);
        tweetsEntityPK1.setEmployeeId("3947d38");
        tweetsEntityPK1.setTweetId(123);
        tweetsEntityPK.equals(tweetsEntityPK1);
        tweetsEntityPK1.setEmployeeId("394738");
        tweetsEntityPK1.setTweetId(1234);
        tweetsEntityPK.equals(tweetsEntityPK1);
        tweetsEntityPK.getEmployeeId();
        tweetsEntityPK.getTweetId();

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

        TweetsEntityWrapper tweetsEntityWrapper = new TweetsEntityWrapper();
        tweetsEntityWrapper.setTweetsEntity(tweetsEntity);
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
        tweet.getEmployeeId();

        Tweets tweets = new Tweets();
        tweets.getTweets();
    }

    private void getTweets_Fallback() {
        IntweetServiceImpl mockIntweetServiceImpl = Mockito.mock(IntweetServiceImpl.class);
        Mockito.when(mockIntweetServiceImpl.getTweets_Fallback(Matchers.anyString(), Matchers.anyInt(), Matchers.anyInt())).thenReturn(new Tweets());
    }

}