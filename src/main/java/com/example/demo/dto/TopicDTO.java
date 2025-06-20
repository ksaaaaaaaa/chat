package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TopicDTO {
    private String title;
    private String content;
    private String category;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
} 