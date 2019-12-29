package com.intuit.intweet.dao.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "followers", schema = "intweet", catalog = "")
@IdClass(FollowersEntityPK.class)
public class FollowersEntity {
    @Id
    private String employeeId;
    @Id
    private String followerId;
    @Column(name = "created_datetime", nullable = false)
    private Date createdDatetime;

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

    public Date getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(Date createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

}
