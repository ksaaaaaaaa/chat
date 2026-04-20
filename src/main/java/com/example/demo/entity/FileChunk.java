package com.example.demo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FileChunk {
    private Long id;
    private String fileIdentifier;
    private Integer chunkNumber;
    private Long chunkSize;
    private String chunkPath;
    private LocalDateTime createdAt;
}
