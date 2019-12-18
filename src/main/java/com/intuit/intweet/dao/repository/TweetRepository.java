package com.intuit.intweet.dao.repository;

import com.intuit.intweet.dao.entity.TweetsEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends CrudRepository<TweetsEntity, Integer>,
        JpaSpecificationExecutor<TweetsEntity> {

    List<TweetsEntity> findByEmployeeId(String employeeId, Pageable pageable);
}
