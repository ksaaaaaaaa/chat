package com.example.demo.dto;

import lombok.Data;

@Data
public class FileCheckDTO {
    private String fileIdentifier;
    private String md5;
    private Long fileSize;
}
