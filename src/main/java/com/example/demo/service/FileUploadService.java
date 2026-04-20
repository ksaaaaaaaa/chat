package com.example.demo.service;

import com.example.demo.dto.FileCheckDTO;
import com.example.demo.dto.FileChunkDTO;
import com.example.demo.dto.FileUploadDTO;
import com.example.demo.dto.FileUploadResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    FileUploadResponseDTO checkFile(FileCheckDTO fileCheckDTO);
    FileUploadResponseDTO initUpload(FileUploadDTO fileUploadDTO);
    FileUploadResponseDTO uploadChunk(FileChunkDTO fileChunkDTO);
    FileUploadResponseDTO completeUpload(String fileIdentifier);
}
