package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LikeDTO {
    private Integer id;
    private Integer userId;
    private Integer replyId;
    private String replyContent;
    private String replyUsername;
    private Integer topicId;
    private String topicTitle;
    private Integer likesCount;
    private LocalDateTime createdAt;
} 