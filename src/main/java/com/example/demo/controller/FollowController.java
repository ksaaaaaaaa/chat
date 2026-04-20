package com.example.demo.controller;

import com.example.demo.dto.FollowDTO;
import com.example.demo.service.FollowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/follows")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true")
public class FollowController {
    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);

    @Autowired
    private FollowService followService;

    @PostMapping
    public ResponseEntity<?> follow(@RequestBody Map<String, Integer> request) {
        try {
            Integer followerId = request.get("followerId");
            Integer followingId = request.get("followingId");
            
            logger.info("收到关注请求, followerId: {}, followingId: {}", followerId, followingId);
            followService.follow(followerId, followingId);
            
            int followingCount = followService.getFollowingCount(followerId);
            int followersCount = followService.getFollowersCount(followingId);
            
            logger.info("关注成功, followerId: {}, followingId: {}", followerId, followingId);
            return ResponseEntity.ok(Map.of(
                "message", "关注成功",
                "followingCount", followingCount,
                "followersCount", followersCount
            ));
        } catch (Exception e) {
            logger.error("关注失败", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> unfollow(@RequestBody Map<String, Integer> request) {
        try {
            Integer followerId = request.get("followerId");
            Integer followingId = request.get("followingId");
            
            logger.info("收到取消关注请求, followerId: {}, followingId: {}", followerId, followingId);
            followService.unfollow(followerId, followingId);
            
            int followingCount = followService.getFollowingCount(followerId);
            int followersCount = followService.getFollowersCount(followingId);
            
            logger.info("取消关注成功, followerId: {}, followingId: {}", followerId, followingId);
            return ResponseEntity.ok(Map.of(
                "message", "取消关注成功",
                "followingCount", followingCount,
                "followersCount", followersCount
            ));
        } catch (Exception e) {
            logger.error("取消关注失败", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkFollow(
            @RequestParam Integer followerId,
            @RequestParam Integer followingId) {
        boolean isFollowing = followService.isFollowing(followerId, followingId);
        return ResponseEntity.ok(Map.of("isFollowing", isFollowing));
    }

    @GetMapping("/users/{userId}/following")
    public ResponseEntity<?> getFollowingList(@PathVariable Integer userId) {
        try {
            logger.info("接收到获取关注列表请求, userId: {}", userId);
            List<FollowDTO> followingList = followService.getFollowingList(userId);
            logger.info("成功获取到关注列表, userId: {}, count: {}", userId, followingList.size());
            return ResponseEntity.ok(followingList);
        } catch (Exception e) {
            logger.error("获取关注列表失败, userId: {}", userId, e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/users/{userId}/followers")
    public ResponseEntity<?> getFollowersList(@PathVariable Integer userId) {
        try {
            logger.info("接收到获取粉丝列表请求, userId: {}", userId);
            List<FollowDTO> followersList = followService.getFollowersList(userId);
            logger.info("成功获取到粉丝列表, userId: {}, count: {}", userId, followersList.size());
            return ResponseEntity.ok(followersList);
        } catch (Exception e) {
            logger.error("获取粉丝列表失败, userId: {}", userId, e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/users/{userId}/count")
    public ResponseEntity<Map<String, Integer>> getFollowCounts(@PathVariable Integer userId) {
        int followingCount = followService.getFollowingCount(userId);
        int followersCount = followService.getFollowersCount(userId);
        
        Map<String, Integer> counts = new HashMap<>();
        counts.put("followingCount", followingCount);
        counts.put("followersCount", followersCount);
        
        return ResponseEntity.ok(counts);
    }
} 
