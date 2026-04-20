package com.example.demo.service.impl;

import com.example.demo.dto.FileCheckDTO;
import com.example.demo.dto.FileChunkDTO;
import com.example.demo.dto.FileUploadDTO;
import com.example.demo.dto.FileUploadResponseDTO;
import com.example.demo.entity.FileChunk;
import com.example.demo.entity.FileInfo;
import com.example.demo.mapper.FileChunkMapper;
import com.example.demo.mapper.FileInfoMapper;
import com.example.demo.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);
    
    private static final long MAX_FILE_SIZE = 15 * 1024 * 1024;
    
    @Value("${file.upload.temp-path:${java.io.tmpdir}/file-upload/temp}")
    private String tempPath;
    
    @Value("${file.upload.final-path:${java.io.tmpdir}/file-upload/final}")
    private String finalPath;
    
    @Autowired
    private FileInfoMapper fileInfoMapper;
    
    @Autowired
    private FileChunkMapper fileChunkMapper;
    
    private static final int STATUS_UPLOADING = 0;
    private static final int STATUS_COMPLETED = 1;
    private static final int STATUS_FAILED = 2;
    
    @Override
    public FileUploadResponseDTO checkFile(FileCheckDTO fileCheckDTO) {
        logger.info("检查文件: fileIdentifier={}, md5={}", fileCheckDTO.getFileIdentifier(), fileCheckDTO.getMd5());
        
        if (fileCheckDTO.getFileSize() != null && fileCheckDTO.getFileSize() > MAX_FILE_SIZE) {
            logger.warn("文件大小超过限制: {} > {}", fileCheckDTO.getFileSize(), MAX_FILE_SIZE);
            return FileUploadResponseDTO.fileTooLarge("文件大小超过限制，最大允许15MB");
        }
        
        if (fileCheckDTO.getMd5() != null && !fileCheckDTO.getMd5().isEmpty()) {
            FileInfo existingFile = fileInfoMapper.findByMd5(fileCheckDTO.getMd5());
            if (existingFile != null && existingFile.getStatus() == STATUS_COMPLETED) {
                logger.info("文件已存在，MD5: {}", fileCheckDTO.getMd5());
                return FileUploadResponseDTO.fileExists("文件已上传过，无需重复上传");
            }
        }
        
        if (fileCheckDTO.getFileIdentifier() != null && !fileCheckDTO.getFileIdentifier().isEmpty()) {
            FileInfo existingFile = fileInfoMapper.findByFileIdentifier(fileCheckDTO.getFileIdentifier());
            if (existingFile != null) {
                int uploadedChunks = fileChunkMapper.countByFileIdentifier(fileCheckDTO.getFileIdentifier());
                logger.info("文件正在上传中，已上传分片数: {}", uploadedChunks);
                return FileUploadResponseDTO.chunkUploaded("文件正在上传中，支持断点续传", uploadedChunks);
            }
        }
        
        logger.info("文件未上传过，可以开始上传");
        return FileUploadResponseDTO.success("文件未上传过，可以开始上传", null);
    }
    
    @Override
    @Transactional
    public FileUploadResponseDTO initUpload(FileUploadDTO fileUploadDTO) {
        logger.info("初始化文件上传: fileName={}, fileSize={}", fileUploadDTO.getFileName(), fileUploadDTO.getFileSize());
        
        if (fileUploadDTO.getFileSize() > MAX_FILE_SIZE) {
            logger.warn("文件大小超过限制: {} > {}", fileUploadDTO.getFileSize(), MAX_FILE_SIZE);
            return FileUploadResponseDTO.fileTooLarge("文件大小超过限制，最大允许15MB");
        }
        
        if (fileUploadDTO.getMd5() != null && !fileUploadDTO.getMd5().isEmpty()) {
            FileInfo existingFile = fileInfoMapper.findByMd5(fileUploadDTO.getMd5());
            if (existingFile != null && existingFile.getStatus() == STATUS_COMPLETED) {
                logger.info("文件已存在，MD5: {}", fileUploadDTO.getMd5());
                return FileUploadResponseDTO.fileExists("文件已上传过，无需重复上传");
            }
        }
        
        String fileIdentifier = fileUploadDTO.getFileIdentifier();
        if (fileIdentifier == null || fileIdentifier.isEmpty()) {
            fileIdentifier = UUID.randomUUID().toString().replace("-", "");
        }
        
        FileInfo existingFile = fileInfoMapper.findByFileIdentifier(fileIdentifier);
        if (existingFile != null) {
            int uploadedChunks = fileChunkMapper.countByFileIdentifier(fileIdentifier);
            logger.info("文件已存在上传记录，已上传分片数: {}", uploadedChunks);
            return FileUploadResponseDTO.chunkUploaded("文件已存在上传记录，支持断点续传", uploadedChunks);
        }
        
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileIdentifier(fileIdentifier);
        fileInfo.setFileName(fileUploadDTO.getFileName());
        fileInfo.setFileSize(fileUploadDTO.getFileSize());
        fileInfo.setFileType(fileUploadDTO.getFileType());
        fileInfo.setMd5(fileUploadDTO.getMd5());
        fileInfo.setTotalChunks(fileUploadDTO.getTotalChunks());
        fileInfo.setStatus(STATUS_UPLOADING);
        
        fileInfoMapper.insert(fileInfo);
        logger.info("文件上传初始化成功，fileIdentifier: {}", fileIdentifier);
        
        return FileUploadResponseDTO.success("文件上传初始化成功", fileIdentifier);
    }
    
    @Override
    @Transactional
    public FileUploadResponseDTO uploadChunk(FileChunkDTO fileChunkDTO) {
        String fileIdentifier = fileChunkDTO.getFileIdentifier();
        Integer chunkNumber = fileChunkDTO.getChunkNumber();
        MultipartFile file = fileChunkDTO.getFile();
        
        logger.info("上传分片: fileIdentifier={}, chunkNumber={}", fileIdentifier, chunkNumber);
        
        FileInfo fileInfo = fileInfoMapper.findByFileIdentifier(fileIdentifier);
        if (fileInfo == null) {
            logger.error("文件信息不存在: {}", fileIdentifier);
            return FileUploadResponseDTO.error("文件上传未初始化，请先初始化上传");
        }
        
        FileChunk existingChunk = fileChunkMapper.findByFileIdentifierAndChunkNumber(fileIdentifier, chunkNumber);
        if (existingChunk != null) {
            logger.info("分片已存在，跳过上传: chunkNumber={}", chunkNumber);
            int uploadedChunks = fileChunkMapper.countByFileIdentifier(fileIdentifier);
            return FileUploadResponseDTO.chunkUploaded("分片已存在", uploadedChunks);
        }
        
        try {
            Path tempDir = Paths.get(tempPath, fileIdentifier);
            if (!Files.exists(tempDir)) {
                Files.createDirectories(tempDir);
            }
            
            String chunkFileName = fileIdentifier + "_" + chunkNumber;
            Path chunkPath = tempDir.resolve(chunkFileName);
            
            file.transferTo(chunkPath.toFile());
            logger.info("分片保存成功: {}", chunkPath);
            
            FileChunk fileChunk = new FileChunk();
            fileChunk.setFileIdentifier(fileIdentifier);
            fileChunk.setChunkNumber(chunkNumber);
            fileChunk.setChunkSize(file.getSize());
            fileChunk.setChunkPath(chunkPath.toString());
            
            fileChunkMapper.insert(fileChunk);
            
            int uploadedChunks = fileChunkMapper.countByFileIdentifier(fileIdentifier);
            logger.info("分片上传成功，已上传分片数: {}/{}", uploadedChunks, fileInfo.getTotalChunks());
            
            if (uploadedChunks >= fileInfo.getTotalChunks()) {
                return FileUploadResponseDTO.success("所有分片上传完成，可以合并文件", uploadedChunks);
            }
            
            return FileUploadResponseDTO.chunkUploaded("分片上传成功", uploadedChunks);
            
        } catch (IOException e) {
            logger.error("分片上传失败", e);
            fileInfo.setStatus(STATUS_FAILED);
            fileInfoMapper.update(fileInfo);
            return FileUploadResponseDTO.error("分片上传失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public FileUploadResponseDTO completeUpload(String fileIdentifier) {
        logger.info("完成文件上传: fileIdentifier={}", fileIdentifier);
        
        FileInfo fileInfo = fileInfoMapper.findByFileIdentifier(fileIdentifier);
        if (fileInfo == null) {
            logger.error("文件信息不存在: {}", fileIdentifier);
            return FileUploadResponseDTO.error("文件信息不存在");
        }
        
        if (fileInfo.getStatus() == STATUS_COMPLETED) {
            logger.info("文件已完成上传: {}", fileIdentifier);
            return FileUploadResponseDTO.success("文件已完成上传", fileInfo);
        }
        
        List<FileChunk> chunks = fileChunkMapper.findByFileIdentifier(fileIdentifier);
        int uploadedChunks = chunks.size();
        int totalChunks = fileInfo.getTotalChunks() != null ? fileInfo.getTotalChunks() : 1;
        
        if (uploadedChunks < totalChunks) {
            logger.warn("分片不完整，已上传: {}, 总共需要: {}", uploadedChunks, totalChunks);
            return FileUploadResponseDTO.chunkUploaded("分片不完整，请继续上传剩余分片", uploadedChunks);
        }
        
        try {
            Path finalDir = Paths.get(finalPath);
            if (!Files.exists(finalDir)) {
                Files.createDirectories(finalDir);
            }
            
            String finalFileName = fileIdentifier + "_" + fileInfo.getFileName();
            Path finalFilePath = finalDir.resolve(finalFileName);
            
            try (BufferedOutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(finalFilePath.toFile()))) {
                
                for (int i = 0; i < totalChunks; i++) {
                    FileChunk chunk = findChunkByNumber(chunks, i);
                    if (chunk == null) {
                        throw new RuntimeException("缺少分片: " + i);
                    }
                    
                    Path chunkPath = Paths.get(chunk.getChunkPath());
                    try (BufferedInputStream inputStream = new BufferedInputStream(
                            new FileInputStream(chunkPath.toFile()))) {
                        
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }
            
            logger.info("文件合并成功: {}", finalFilePath);
            
            fileInfo.setFilePath(finalFilePath.toString());
            fileInfo.setStatus(STATUS_COMPLETED);
            fileInfoMapper.update(fileInfo);
            
            fileChunkMapper.deleteByFileIdentifier(fileIdentifier);
            deleteTempDirectory(fileIdentifier);
            
            logger.info("文件上传完成: {}", fileIdentifier);
            
            return FileUploadResponseDTO.success("文件上传完成", fileInfo);
            
        } catch (IOException e) {
            logger.error("文件合并失败", e);
            fileInfo.setStatus(STATUS_FAILED);
            fileInfoMapper.update(fileInfo);
            return FileUploadResponseDTO.error("文件合并失败: " + e.getMessage());
        }
    }
    
    private FileChunk findChunkByNumber(List<FileChunk> chunks, int chunkNumber) {
        for (FileChunk chunk : chunks) {
            if (chunk.getChunkNumber() == chunkNumber) {
                return chunk;
            }
        }
        return null;
    }
    
    private void deleteTempDirectory(String fileIdentifier) {
        try {
            Path tempDir = Paths.get(tempPath, fileIdentifier);
            if (Files.exists(tempDir)) {
                Files.walk(tempDir)
                    .map(Path::toFile)
                    .forEach(File::delete);
                Files.deleteIfExists(tempDir);
            }
        } catch (IOException e) {
            logger.warn("删除临时目录失败: {}", fileIdentifier, e);
        }
    }
}
