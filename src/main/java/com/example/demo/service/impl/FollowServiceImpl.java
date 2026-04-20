package com.example.demo.service.impl;

import com.example.demo.dto.FollowDTO;
import com.example.demo.entity.Follow;
import com.example.demo.entity.User;
import com.example.demo.mapper.FollowMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {
    private static final Logger logger = LoggerFactory.getLogger(FollowServiceImpl.class);

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public void follow(Integer followerId, Integer followingId) {
        try {
            if (followerId.equals(followingId)) {
                throw new RuntimeException("不能关注自己");
            }
            
            User follower = userMapper.findById(followerId);
            if (follower == null) {
                throw new RuntimeException("关注者不存在");
            }
            
            User following = userMapper.findById(followingId);
            if (following == null) {
                throw new RuntimeException("被关注者不存在");
            }
            
            if (!followMapper.exists(followerId, followingId)) {
                Follow follow = new Follow();
                follow.setFollowerId(followerId);
                follow.setFollowingId(followingId);
                followMapper.insert(follow);
                
                int currentFollowingCount = followMapper.countFollowing(followerId);
                userMapper.updateFollowingCount(followerId, currentFollowingCount);
                
                int currentFollowersCount = followMapper.countFollowers(followingId);
                userMapper.updateFollowersCount(followingId, currentFollowersCount);
                
                logger.info("用户 {} 成功关注用户 {}", followerId, followingId);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("关注失败, followerId: {}, followingId: {}", followerId, followingId, e);
            throw new RuntimeException("关注失败", e);
        }
    }

    @Override
    @Transactional
    public void unfollow(Integer followerId, Integer followingId) {
        try {
            if (followMapper.exists(followerId, followingId)) {
                followMapper.delete(followerId, followingId);
                
                int currentFollowingCount = followMapper.countFollowing(followerId);
                userMapper.updateFollowingCount(followerId, currentFollowingCount);
                
                int currentFollowersCount = followMapper.countFollowers(followingId);
                userMapper.updateFollowersCount(followingId, currentFollowersCount);
                
                logger.info("用户 {} 成功取消关注用户 {}", followerId, followingId);
            }
        } catch (Exception e) {
            logger.error("取消关注失败, followerId: {}, followingId: {}", followerId, followingId, e);
            throw new RuntimeException("取消关注失败", e);
        }
    }

    @Override
    public boolean isFollowing(Integer followerId, Integer followingId) {
        return followMapper.exists(followerId, followingId);
    }

    @Override
    public List<FollowDTO> getFollowingList(Integer userId) {
        return followMapper.findFollowingByUserId(userId);
    }

    @Override
    public List<FollowDTO> getFollowersList(Integer userId) {
        return followMapper.findFollowersByUserId(userId);
    }

    @Override
    public int getFollowingCount(Integer userId) {
        return followMapper.countFollowing(userId);
    }

    @Override
    public int getFollowersCount(Integer userId) {
        return followMapper.countFollowers(userId);
    }
} 
