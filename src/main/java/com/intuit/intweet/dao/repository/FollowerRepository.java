package com.intuit.intweet.dao.repository;

import com.intuit.intweet.dao.entity.FollowersEntity;
import com.intuit.intweet.dao.entity.FollowersEntityPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowerRepository extends CrudRepository<FollowersEntity, FollowersEntityPK> {

    List<FollowersEntity> findByFollowerId(String followerId);

    List<FollowersEntity> findByEmployeeId(String employeeId);
}
