package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FavoriteDTO {
    private Integer id;
    private Integer userId;
    private Integer topicId;
    private String topicTitle;
    private String topicContent;
    private Integer viewCount;
    private Integer replyCount;
    private Integer favoritesCount;
    private LocalDateTime topicCreatedAt;
    private LocalDateTime createdAt;  // 收藏时间
} 