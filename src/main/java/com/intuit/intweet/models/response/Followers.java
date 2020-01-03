package com.intuit.intweet.models.response;

import java.util.ArrayList;
import java.util.List;

public class Followers {

    private List<Follower> followers = new ArrayList<>();

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }
}
