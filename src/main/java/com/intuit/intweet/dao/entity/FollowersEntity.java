package com.intuit.intweet.dao.entity;

import javax.persistence.*;

@Entity
@Table(name = "followers", schema = "intweet", catalog = "")
@IdClass(FollowersEntityPK.class)
public class FollowersEntity {
    @Id
    private String employeeId;
    @Id
    private String followerId;

    public FollowersEntity() {

    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

}
