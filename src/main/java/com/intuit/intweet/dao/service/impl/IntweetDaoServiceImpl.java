package com.intuit.intweet.dao.service.impl;

import com.intuit.intweet.dao.entity.EmployeesEntity;
import com.intuit.intweet.dao.entity.FollowersEntity;
import com.intuit.intweet.dao.entity.FollowersEntityPK;
import com.intuit.intweet.dao.entity.TweetsEntity;
import com.intuit.intweet.dao.repository.EmployeeRepository;
import com.intuit.intweet.dao.repository.FollowerRepository;
import com.intuit.intweet.dao.repository.TweetRepository;
import com.intuit.intweet.dao.service.IntweetDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntweetDaoServiceImpl implements IntweetDaoService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private FollowerRepository followerRepository;

    @Override
    public EmployeesEntity findEmployeeByEmployeeId(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<FollowersEntity> findAllByFollowerId(String followerId) {
        return followerRepository.findByFollowerId(followerId);
    }

    @Override
    public List<FollowersEntity> findAllByFollowingId(String employeeId) {
        return followerRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<TweetsEntity> findTweetsByEmployeeId(String employeeId, int offset, int limit) {
        Pageable paging = PageRequest.of(offset, limit, Sort.Direction.DESC, "createdDatetime");
        return tweetRepository.findByEmployeeId(employeeId, paging);
    }

    @Override
    public Page<TweetsEntity> findByCriteria(Specification<TweetsEntity> specification, int offset, int limit) {
        Pageable paging = PageRequest.of(offset, limit, Sort.Direction.DESC, "createdDatetime");
        return tweetRepository.findAll(specification, paging);
    }

    @Override
    public TweetsEntity saveTweet(TweetsEntity tweetsEntity) {
        return tweetRepository.save(tweetsEntity);
    }

    @Override
    public void deleteTweet(int tweetId) {
        tweetRepository.deleteById(tweetId);
    }

    @Override
    public FollowersEntity saveFollowersEntity(FollowersEntity followersEntity) {
        return followerRepository.save(followersEntity);
    }

    @Override
    public void deleteFollower(FollowersEntityPK followersEntityPK) {
        followerRepository.deleteById(followersEntityPK);
    }

}
