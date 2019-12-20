package com.intuit.intweet.dao.service;

import com.intuit.intweet.dao.entity.EmployeesEntity;
import com.intuit.intweet.dao.entity.FollowersEntity;
import com.intuit.intweet.dao.entity.FollowersEntityPK;
import com.intuit.intweet.dao.entity.TweetsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface IntweetDaoService {

    EmployeesEntity findEmployeeByEmployeeId(String employeeId);

    List<FollowersEntity> findAllByFollowerId(String followerId);

    List<FollowersEntity> findAllByFollowingId(String employeeId);

    List<TweetsEntity> findTweetsByEmployeeIdIn(List<String> employeeIds, int offset, int limit);

    List<TweetsEntity> findTweetsByEmployeeId(String employeeId, int offset, int limit);

    Page<TweetsEntity> findByCriteria(Specification<TweetsEntity> specification, int offset, int limit);

    TweetsEntity saveTweet(TweetsEntity tweetsEntity);

    void deleteTweet(String employeeID, int tweetId);

    FollowersEntity saveFollowersEntity(FollowersEntity followersEntity);

    void deleteFollower(FollowersEntityPK followersEntityPK);


}
