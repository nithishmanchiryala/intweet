package com.intuit.intweet.resources;

import com.intuit.intweet.exceptions.CustomException;
import com.intuit.intweet.models.request.CreateTweetRequest;
import com.intuit.intweet.models.response.Followers;
import com.intuit.intweet.models.response.Tweets;
import com.intuit.intweet.services.IntweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class IntweetResource {

    @Autowired
    private IntweetService intweetService;

    @GetMapping("/feed")
    public Tweets getAllTweets(@RequestParam int offset, @RequestParam int limit) {
        return intweetService.getLatestTweets(offset, limit);
    }

    @GetMapping("/feed/{employeeID}")
    public Tweets getTweets(@PathVariable String employeeID, @RequestParam int offset, @RequestParam int limit) throws CustomException {
        return intweetService.getTweets(employeeID, offset, limit);
    }

    @GetMapping("/feed/me/{employeeID}")
    public Tweets getMyTweets(@PathVariable String employeeID, @RequestParam int offset, @RequestParam int limit) throws CustomException {
        return intweetService.getEmployeeTweets(employeeID, offset, limit);
    }

    @PostMapping("/feed")
    public void createOrUpdateTweet(@Valid @RequestBody CreateTweetRequest createTweetRequest, @RequestParam(required = false) String tweetID) throws CustomException {
        intweetService.postTweet(createTweetRequest, tweetID);
    }

    @DeleteMapping("/feed/{employeeID}/{tweetID}")
    public void deleteTweet(@PathVariable String employeeID, @PathVariable int tweetID) throws CustomException {
        intweetService.deleteTweet(employeeID, tweetID);
    }

    @PutMapping("/follow/{employeeID}/{followerID}")
    public void followEmployee(@PathVariable String employeeID, @PathVariable String followerID) throws CustomException {
        intweetService.followEmployee(employeeID, followerID);
    }

    @GetMapping("/followers/{employeeID}")
    public Followers getFollowers(@PathVariable String employeeID) throws CustomException {
        return intweetService.getFollowers(employeeID);
    }

    @DeleteMapping("/follow/{employeeID}/{followerID}")
    public void unfollowEmployee(@PathVariable String employeeID, @PathVariable String followerID) throws CustomException {
        intweetService.unfollowEmployee(employeeID, followerID);
    }

    @GetMapping("/following/{employeeID}")
    public Followers getFollowing(@PathVariable String employeeID) throws CustomException {
        return intweetService.getFollowing(employeeID);
    }
}
