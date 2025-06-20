package com.example.demo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Reply {
    private Integer id;
    private String content;
    private Integer userId;
    private Integer topicId;
    private Integer likesCount;
    private LocalDateTime createdAt;
    
    // 非数据库字段
    private String username;
    private Boolean isLiked;
} 