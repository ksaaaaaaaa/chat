package com.example.demo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileChunkDTO {
    private String fileIdentifier;
    private Integer chunkNumber;
    private Long chunkSize;
    private Integer totalChunks;
    private MultipartFile file;
}
