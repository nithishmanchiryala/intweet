package com.intuit.intweet.dao.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "followers", schema = "intweet", catalog = "")
@IdClass(FollowersEntityPK.class)
public class FollowersEntity {
    private int uidpk;
    @Id
    private String employeeId;
    @Id
    private String followerId;
    private Date createdDatetime;

    public FollowersEntity() {

    }

    @Column(name = "uidpk", nullable = false)
    public int getUidpk() {
        return uidpk;
    }

    public void setUidpk(int uidpk) {
        this.uidpk = uidpk;
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

    @Basic
    @Column(name = "created_datetime", nullable = false)
    public Date getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(Date createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

}
