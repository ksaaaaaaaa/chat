package com.example.demo.dto;

import lombok.Data;

@Data
public class FileUploadResponseDTO {
    private String code;
    private String message;
    private Object data;
    
    public static FileUploadResponseDTO success(String message, Object data) {
        FileUploadResponseDTO response = new FileUploadResponseDTO();
        response.setCode("200");
        response.setMessage(message);
        response.setData(data);
        return response;
    }
    
    public static FileUploadResponseDTO error(String message) {
        FileUploadResponseDTO response = new FileUploadResponseDTO();
        response.setCode("400");
        response.setMessage(message);
        return response;
    }
    
    public static FileUploadResponseDTO fileExists(String message) {
        FileUploadResponseDTO response = new FileUploadResponseDTO();
        response.setCode("409");
        response.setMessage(message);
        return response;
    }
    
    public static FileUploadResponseDTO fileTooLarge(String message) {
        FileUploadResponseDTO response = new FileUploadResponseDTO();
        response.setCode("413");
        response.setMessage(message);
        return response;
    }
    
    public static FileUploadResponseDTO chunkUploaded(String message, Integer uploadedChunks) {
        FileUploadResponseDTO response = new FileUploadResponseDTO();
        response.setCode("206");
        response.setMessage(message);
        response.setData(uploadedChunks);
        return response;
    }
}
