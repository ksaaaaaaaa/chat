package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FollowDTO {
    private Integer id;
    private Integer followerId;
    private Integer followingId;
    private String followingUsername;
    private String followingEmail;
    private String followingRole;
    private Integer followingFavoritesCount;
    private Integer followingLikesCount;
    private Integer followingFollowingCount;
    private Integer followingFollowersCount;
    private LocalDateTime followingCreatedAt;
    private LocalDateTime createdAt;
} 
