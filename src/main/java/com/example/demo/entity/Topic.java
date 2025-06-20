package com.example.demo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Topic {
    private Integer id;
    private String title;
    private String content;
    private Integer userId;
    private String category;
    private Integer viewCount;
    private Integer replyCount;
    private Integer favoritesCount;
    private LocalDateTime lastReplyTime;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 非数据库字段
    private String username;
} 