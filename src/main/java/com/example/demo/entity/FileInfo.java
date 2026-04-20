package com.example.demo.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FileInfo {
    private Long id;
    private String fileIdentifier;
    private String fileName;
    private Long fileSize;
    private String fileType;
    private String filePath;
    private String md5;
    private Integer status;
    private Integer totalChunks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
