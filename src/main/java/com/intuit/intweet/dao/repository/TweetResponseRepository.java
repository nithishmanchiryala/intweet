package com.intuit.intweet.dao.repository;

import com.intuit.intweet.dao.entity.TweetResponsesEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetResponseRepository extends CrudRepository<TweetResponsesEntity, Integer> {
}
