package com.example.demo.dto;

import lombok.Data;

@Data
public class FileUploadDTO {
    private String fileIdentifier;
    private String fileName;
    private Long fileSize;
    private String fileType;
    private String md5;
    private Integer totalChunks;
}
