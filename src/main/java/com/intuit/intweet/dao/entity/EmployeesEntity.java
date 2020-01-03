package com.intuit.intweet.dao.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "employees", schema = "intweet", catalog = "")
public class EmployeesEntity {
    private String employeeId;
    private String firstName;
    private String lastName;
    private Date createdDatetime;
    private Date lastModifiedDatetime;
    private List<FollowersEntity> followersByEmployeeId;
    private List<TweetsEntity> tweetsByEmployeeId;

    public EmployeesEntity() {

    }

    @Id
    @Column(name = "employee_id", nullable = false, length = 20)
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @Column(name = "first_name", nullable = false, length = 20)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", nullable = false, length = 20)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "created_datetime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(Date createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    @Column(name = "last_modified_datetime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastModifiedDatetime() {
        return lastModifiedDatetime;
    }

    public void setLastModifiedDatetime(Date lastModifiedDatetime) {
        this.lastModifiedDatetime = lastModifiedDatetime;
    }

    @Transient
    @OneToMany(mappedBy = "employees", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<FollowersEntity> getFollowersByEmployeeId() {
        return followersByEmployeeId;
    }


    public void setFollowersByEmployeeId(List<FollowersEntity> followersByEmployeeId) {
        this.followersByEmployeeId = followersByEmployeeId;
    }

    @Transient
    @OneToMany(mappedBy = "employees", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<TweetsEntity> getTweetsByEmployeeId() {
        return tweetsByEmployeeId;
    }

    public void setTweetsByEmployeeId(List<TweetsEntity> tweetsByEmployeeId) {
        this.tweetsByEmployeeId = tweetsByEmployeeId;
    }
}
