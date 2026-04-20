package com.example.demo.controller;

import com.example.demo.dto.FileCheckDTO;
import com.example.demo.dto.FileChunkDTO;
import com.example.demo.dto.FileUploadDTO;
import com.example.demo.dto.FileUploadResponseDTO;
import com.example.demo.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true", allowedHeaders = "*")
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/check")
    public ResponseEntity<?> checkFile(@RequestBody FileCheckDTO fileCheckDTO) {
        logger.info("检查文件: fileIdentifier={}, md5={}, fileSize={}", 
            fileCheckDTO.getFileIdentifier(), fileCheckDTO.getMd5(), fileCheckDTO.getFileSize());
        
        try {
            FileUploadResponseDTO response = fileUploadService.checkFile(fileCheckDTO);
            return buildResponse(response);
        } catch (Exception e) {
            logger.error("检查文件失败", e);
            return ResponseEntity.status(500).body(Map.of("error", "检查文件失败: " + e.getMessage()));
        }
    }

    @PostMapping("/init")
    public ResponseEntity<?> initUpload(@RequestBody FileUploadDTO fileUploadDTO) {
        logger.info("初始化文件上传: fileName={}, fileSize={}, md5={}", 
            fileUploadDTO.getFileName(), fileUploadDTO.getFileSize(), fileUploadDTO.getMd5());
        
        try {
            FileUploadResponseDTO response = fileUploadService.initUpload(fileUploadDTO);
            return buildResponse(response);
        } catch (Exception e) {
            logger.error("初始化文件上传失败", e);
            return ResponseEntity.status(500).body(Map.of("error", "初始化失败: " + e.getMessage()));
        }
    }

    @PostMapping("/chunk")
    public ResponseEntity<?> uploadChunk(
            @RequestParam("fileIdentifier") String fileIdentifier,
            @RequestParam("chunkNumber") Integer chunkNumber,
            @RequestParam(value = "chunkSize", required = false) Long chunkSize,
            @RequestParam(value = "totalChunks", required = false) Integer totalChunks,
            @RequestParam("file") MultipartFile file) {
        
        logger.info("上传分片: fileIdentifier={}, chunkNumber={}", fileIdentifier, chunkNumber);
        
        try {
            FileChunkDTO fileChunkDTO = new FileChunkDTO();
            fileChunkDTO.setFileIdentifier(fileIdentifier);
            fileChunkDTO.setChunkNumber(chunkNumber);
            fileChunkDTO.setChunkSize(chunkSize);
            fileChunkDTO.setTotalChunks(totalChunks);
            fileChunkDTO.setFile(file);
            
            FileUploadResponseDTO response = fileUploadService.uploadChunk(fileChunkDTO);
            return buildResponse(response);
        } catch (Exception e) {
            logger.error("上传分片失败", e);
            return ResponseEntity.status(500).body(Map.of("error", "上传分片失败: " + e.getMessage()));
        }
    }

    @PostMapping("/complete")
    public ResponseEntity<?> completeUpload(@RequestParam("fileIdentifier") String fileIdentifier) {
        logger.info("完成文件上传: fileIdentifier={}", fileIdentifier);
        
        try {
            FileUploadResponseDTO response = fileUploadService.completeUpload(fileIdentifier);
            return buildResponse(response);
        } catch (Exception e) {
            logger.error("完成文件上传失败", e);
            return ResponseEntity.status(500).body(Map.of("error", "完成上传失败: " + e.getMessage()));
        }
    }

    private ResponseEntity<?> buildResponse(FileUploadResponseDTO response) {
        String code = response.getCode();
        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("message", response.getMessage());
        if (response.getData() != null) {
            result.put("data", response.getData());
        }
        
        switch (code) {
            case "200":
                return ResponseEntity.ok(result);
            case "206":
                return ResponseEntity.status(206).body(result);
            case "409":
                return ResponseEntity.status(409).body(result);
            case "413":
                return ResponseEntity.status(413).body(result);
            case "400":
                return ResponseEntity.badRequest().body(result);
            default:
                return ResponseEntity.status(500).body(result);
        }
    }
}
