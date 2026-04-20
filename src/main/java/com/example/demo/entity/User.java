package com.example.demo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String role;
    private String status;
    private Integer favoritesCount;
    private Integer likesCount;
    private Integer followingCount;
    private Integer followersCount;
    private LocalDateTime createdAt;
} 