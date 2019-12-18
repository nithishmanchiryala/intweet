package com.intuit.intweet.dao.entity;

import java.io.Serializable;
import java.util.Objects;

public class FollowersEntityPK implements Serializable {
    private String employeeId;
    private String followerId;

    public FollowersEntityPK() {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowersEntityPK that = (FollowersEntityPK) o;
        return Objects.equals(employeeId, that.employeeId) &&
                Objects.equals(followerId, that.followerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, followerId);
    }
}
