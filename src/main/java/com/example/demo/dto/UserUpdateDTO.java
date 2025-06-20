package com.example.demo.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String username;
    private String email;
    private String currentPassword;
    private String newPassword;
} 