package com.intuit.intweet.resources;

import com.intuit.intweet.exceptions.EmployeeNotFoundException;
import com.intuit.intweet.models.request.CreateTweetRequest;
import com.intuit.intweet.models.response.Follower;
import com.intuit.intweet.models.response.Tweets;
import com.intuit.intweet.services.IntweetService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class IntweetResource {

    private static final Logger logger = LogManager.getLogger(IntweetResource.class);

    @Autowired
    private IntweetService intweetService;

    @GetMapping("/feed")
    public ResponseEntity<Tweets> getLatestTweets(@RequestParam int offset, @RequestParam int limit) {
        return new ResponseEntity<>(intweetService.getLatestTweets(offset, limit), HttpStatus.OK);
    }

    @GetMapping("/feed/{employeeID}")
    public ResponseEntity<Tweets> getTweets(@PathVariable String employeeID, @RequestParam int offset, @RequestParam int limit) {
        try {
            return new ResponseEntity<>(intweetService.getTweets(employeeID, offset, limit), HttpStatus.OK);
        } catch (EmployeeNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/feed/me/{employeeID}")
    public ResponseEntity<Tweets> getMyTweets(@PathVariable String employeeID, @RequestParam int offset, @RequestParam int limit) {
        try {
            return new ResponseEntity<>(intweetService.getEmployeeTweets(employeeID, offset, limit), HttpStatus.OK);
        } catch (EmployeeNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/feed")
    public ResponseEntity createOrUpdateTweet(@Valid @RequestBody CreateTweetRequest createTweetRequest,
                                      @RequestParam(required = false) String tweetID) {
        HttpStatus httpStatus = intweetService.postTweet(createTweetRequest, tweetID);
        return new ResponseEntity<>(httpStatus);
    }

    @DeleteMapping("/feed/{employeeID}/{tweetID}")
    public ResponseEntity deleteTweet(@PathVariable String employeeID, @PathVariable int tweetID) {
        HttpStatus httpStatus = intweetService.deleteTweet(employeeID, tweetID);
        return new ResponseEntity<>(httpStatus);
    }

    @PutMapping("/follow/{employeeID}/{followerID}")
    public ResponseEntity followEmployee(@PathVariable String employeeID, @PathVariable String followerID) {
        HttpStatus httpStatus = intweetService.followEmployee(employeeID, followerID);
        return new ResponseEntity<>(httpStatus);
    }

    @GetMapping("/followers/{employeeID}")
    public ResponseEntity<List<Follower>> getFollowers(@PathVariable String employeeID) {
        try {
            return new ResponseEntity<>(intweetService.getFollowers(employeeID), HttpStatus.OK);
        } catch (EmployeeNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/follow/{employeeID}/{followerID}")
    public ResponseEntity unfollowEmployee(@PathVariable String employeeID, @PathVariable String followerID) {
        HttpStatus httpStatus = intweetService.unfollowEmployee(employeeID, followerID);
        return new ResponseEntity<>(httpStatus);
    }

    @GetMapping("/following/{employeeID}")
    public ResponseEntity<List<Follower>> getFollowing(@PathVariable String employeeID) {
        try {
            return new ResponseEntity<>(intweetService.getFollowing(employeeID), HttpStatus.OK);
        } catch (EmployeeNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
