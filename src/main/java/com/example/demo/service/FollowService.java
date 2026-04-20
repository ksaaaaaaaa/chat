package com.example.demo.service;

import com.example.demo.dto.FollowDTO;
import java.util.List;

public interface FollowService {
    void follow(Integer followerId, Integer followingId);
    void unfollow(Integer followerId, Integer followingId);
    boolean isFollowing(Integer followerId, Integer followingId);
    
    List<FollowDTO> getFollowingList(Integer userId);
    List<FollowDTO> getFollowersList(Integer userId);
    
    int getFollowingCount(Integer userId);
    int getFollowersCount(Integer userId);
} 
